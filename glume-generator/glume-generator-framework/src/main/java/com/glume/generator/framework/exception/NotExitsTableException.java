package com.glume.generator.framework.exception;

import java.io.Serializable;

/**
 * 不存在的数据表异常
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 17:09
 * @Version: v1.0.0
 */
public class NotExitsTableException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public NotExitsTableException(String message) {
        super(message);
    }
}
