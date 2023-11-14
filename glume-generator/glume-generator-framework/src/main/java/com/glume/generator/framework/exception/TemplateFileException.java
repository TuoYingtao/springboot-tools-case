package com.glume.generator.framework.exception;

import java.io.Serializable;

/**
 * 模板文件异常 xxx.xx.ftl
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-14 17:09
 * @Version: v1.0.0
 */
public class TemplateFileException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public TemplateFileException(String message) {
        super(message);
    }
}
