package com.glume.generator.framework.exception;

import java.io.Serializable;

/**
 * 业务异常
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 17:09
 * @Version: v1.0.0
 */
public class ServiceException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public ServiceException(String message) {
        super(message);
    }
}
