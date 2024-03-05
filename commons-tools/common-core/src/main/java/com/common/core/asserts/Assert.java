package com.common.core.asserts;

import com.common.core.constant.Constants;
import com.common.core.constant.HttpStatus;
import com.common.core.domain.Result;
import com.common.core.exception.BaseException;
import com.common.core.utils.StringUtils;

import java.util.Map;

/**
 * 断言异常处理接口
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 15:45:28
 * @Version: v1.0.0
*/
public interface Assert {

    /**
     * 创建异常
     */
    BaseException newException(Object ...args);

    /**
     * 创建异常
     */
    BaseException newException(Throwable t, Object ...args);

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     *
     * @param obj 待判断对象
     */
    default void assertNotNull(Object obj) {
        if (StringUtils.isNull(obj) || StringUtils.isEmpty(obj.toString())) {
            throw newException(obj);
        }
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj 待判断对象
     * @param args message占位符对应的参数列表
     */
    default void assertNotNull(Object obj, Object... args) {
        if (StringUtils.isNull(obj) || StringUtils.isEmpty(obj.toString())) {
            throw newException(args);
        }
    }

    /**
     * 验证current是否在这范围内，若不是则抛出异常
     * @param obj 待判断对象
     * @param min 最小值
     * @param max 最大值
     */
    default void assertNotRange(Object obj, Integer min, Integer max) {
        if (StringUtils.isNull(obj) || StringUtils.isEmpty(obj.toString())) {
            throw newException();
        }
        Integer current = Integer.valueOf(String.valueOf(obj));
        if (!(Math.max(min, current) == Math.min(current, max))) {
            throw newException(obj, min, max);
        }
    }

    /**
     * 验证 R 响应是否成功，如果code非200，则抛出异常
     * @param obj 待判断对象
     */
    default void assertNotSuccessResponse(Object obj) {
        if (StringUtils.isNull(obj) || StringUtils.isEmpty(obj.toString())) {
            throw newException("未知错误");
        }
        Result response = (Result) obj;
        if (response.getCode() != HttpStatus.SUCCESS) {
            throw newException(obj, response.getMsg());
        }
    }

    /**
     * 分页参数 pageNum 若不存在，则抛出异常
     * @param params 分页参数
     */
    default void assertNotPage(Map<String, Object> params) {
        if (!params.containsKey(Constants.PAGE_NUM)) {
            throw newException(params);
        }
    }
}
