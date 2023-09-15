package com.glume.generator.service.aspect;


import com.glume.generator.framework.commons.Result;
import com.glume.generator.framework.commons.encrypt.EncryptUtils;
import com.glume.generator.service.annotaion.EncryptParameter;
import com.glume.generator.service.utils.PageUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 处理参数加密解密切面
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 13:46
 * @Version: v1.0.0
 */
@Aspect
@Component
public class EncryptParameterAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptParameterAspect.class);

    /**
     * 切面方法：page、list、get、save、update、tableList
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.glume.generator.service.controller.DataSourceController.*(..))")
    public Object doProcess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 处理请求入参
        List<Object> methodArgs = this.getMethodArgs(proceedingJoinPoint);
        for (Object item : methodArgs) {
            handleItem(item, true);
        }
        Object result = proceedingJoinPoint.proceed();

        // 处理返回值
        handleObject(result);
        return result;
    }

    /**
     * 获取方法的请求参数
     */
    private List<Object> getMethodArgs(ProceedingJoinPoint proceedingJoinPoint) {
        List<Object> methodArgs = new ArrayList<>();
        for (Object arg : proceedingJoinPoint.getArgs()) {
            if (Objects.nonNull(arg)) {
                methodArgs.add(arg);
            }
        }
        return methodArgs;
    }

    /**
     * 加密返回结果中的字段
     *
     * @param object
     * @throws Exception
     */
    private void handleObject(Object object) throws Exception {
        // 仅处理类型是Result的返回对象
        if (!(object instanceof Result) || Objects.isNull(((Result<?>) object).getData())) {
            return;
        }

        Object data = ((Result<?>) object).getData();
        if (data instanceof List || data instanceof PageUtils) {
            List itemList = data instanceof List ? (List) data : ((PageUtils<?>) data).getList();
            itemList.forEach(f ->
                    handleItem(f, false)
            );
        } else {
            handleItem(data, false);
        }
    }

    /**
     * 加密/解密具体对象下的字段
     *
     * @param item      需要加解密的对象
     * @param isDecrypt true：解密，false：加密
     */
    private void handleItem(Object item, boolean isDecrypt) {

        // 只处理在entity包下面的对象
        if (Objects.isNull(item.getClass().getPackage()) || !item.getClass().getPackage().getName().startsWith("com.glume.generator.service.domain.entity")) {
            return;
        }

        // 遍历所有字段
        Field[] fields = item.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 若该字段被EncryptParameter注解,则进行解密/加密
            Class<?> fieldType = field.getType();
            if (fieldType == String.class && Objects.nonNull(AnnotationUtils.findAnnotation(field, EncryptParameter.class))) {
                // 设置private类型允许访问
                field.setAccessible(Boolean.TRUE);
                try {
                    String newFieldValue = isDecrypt ? EncryptUtils.decrypt((String) field.get(item)) : EncryptUtils.encrypt((String) field.get(item));
                    field.set(item, newFieldValue);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
                field.setAccessible(Boolean.FALSE);
            }
        }
    }
}

