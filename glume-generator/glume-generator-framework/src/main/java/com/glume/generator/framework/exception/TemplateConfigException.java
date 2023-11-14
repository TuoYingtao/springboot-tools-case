package com.glume.generator.framework.exception;

import java.io.Serializable;

/**
 * 模板配置异常 config.json
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 17:09
 * @Version: v1.0.0
 */
public class TemplateConfigException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public TemplateConfigException(String message) {
        super(message);
    }
}
