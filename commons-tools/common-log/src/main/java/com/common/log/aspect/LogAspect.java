package com.common.log.aspect;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson2.JSON;
import com.common.core.constant.Constants;
import com.common.core.constant.TokenConstants;
import com.common.core.text.Convert;
import com.common.core.utils.ExceptionUtil;
import com.common.core.utils.JwtUtils;
import com.common.core.utils.ServletUtils;
import com.common.core.utils.StringUtils;
import com.common.core.utils.ip.IpUtils;
import com.common.log.annotation.Log;
import com.common.log.domain.SysOperateLog;
import com.common.log.enums.BusinessStatus;
import com.common.log.filter.PropertyPreExcludeFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

/**
 * 操作日志记录处理
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-09 16:42
 * @Version: v1.0.0
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREAD_LOCAL = new NamedThreadLocal<Long>("Cost Time");

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREAD_LOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object result) {
        handleLog(joinPoint, controllerLog, null, result);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            //*======== 数据库日志 =========*//
            SysOperateLog operateLog = new SysOperateLog();
            operateLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = IpUtils.getIpAddr();
            operateLog.setOperateIp(ip);
            // 用户设备信息
            String userAgent = ServletUtils.getHeader(ServletUtils.getRequest(), Constants.USER_AGENT);
            if (StringUtils.isNotEmpty(userAgent)) {
                operateLog.setUserAgent(userAgent);
                UserAgent ua = UserAgentUtil.parse(userAgent);
                // 浏览器信息
                operateLog.setBrowserName(ua.getBrowser().getName());
                operateLog.setBrowserVersion(ua.getVersion());
                operateLog.setRenderingEngine(ua.getEngine().getName());
                operateLog.setRenderingEngineVersion(ua.getEngineVersion());
                // 操作系统信息
                operateLog.setOpSysName(ua.getOs().getName());
                operateLog.setOpSysVersion(ua.getOsVersion());
                operateLog.setOpSysPlatform(ua.getPlatform().getName());
            }
            // 请求地址
            operateLog.setOperateUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            String headerAuth = ServletUtils.getHeader(ServletUtils.getRequest(), TokenConstants.AUTHENTICATION);
            if (StringUtils.isNotEmpty(headerAuth) && JwtUtils.isVerify(headerAuth)) {
                String username = JwtUtils.getUserName(headerAuth);
                if (StringUtils.isNotBlank(username)) {
                    operateLog.setOperateName(username);
                }
            }
            if (e != null) {
                operateLog.setStatus(BusinessStatus.FAIL.ordinal());
                operateLog.setErrorMsg(StringUtils.substring(ExceptionUtil.getRootErrorMessage(e), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            String classPathName = Convert.utf8Str(new StringBuilder().append(className).append(".").append(methodName).append("()"));
            operateLog.setMethod(classPathName);
            // 设置请求方式
            operateLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operateLog, jsonResult);
            // 设置消耗时间
            operateLog.setCostTime(System.currentTimeMillis() - TIME_THREAD_LOCAL.get());
            // 保存数据库 TODO
            LOGGER.info(Convert.utf8Str(JSON.toJSONString(operateLog)));
        } catch (Exception exp) {
            // 记录本地异常日志
            LOGGER.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        } finally {
            TIME_THREAD_LOCAL.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operateLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperateLog operateLog, Object jsonResult) throws Exception {
        // 设置action动作
        operateLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operateLog.setTitle(log.title());
        // 设置操作人类别
        operateLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operateLog, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && StringUtils.isNotNull(jsonResult)) {
            operateLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operateLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperateLog operateLog, String[] excludeParamNames) throws Exception {
        String requestMethod = operateLog.getRequestMethod();
        Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        if (StringUtils.isEmpty(paramsMap)
                && (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operateLog.setOperateParam(StringUtils.substring(params, 0, 2000));
        } else {
            operateLog.setOperateParam(StringUtils.substring(JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames)), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (StringUtils.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params.append(Convert.str(jsonObj, StandardCharsets.UTF_8)).append(" ");
                    } catch (Exception e) {
                    }
                }
            }
        }
        return Convert.str(params, StandardCharsets.UTF_8).trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
