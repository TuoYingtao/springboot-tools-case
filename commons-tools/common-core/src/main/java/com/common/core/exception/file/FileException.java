package com.common.core.exception.file;

import com.common.core.exception.BaseException;

/**
 * 文件信息异常类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-13 10:40
 * @Version: v1.0.0
 */
public class FileException extends BaseException {

    private static final long serialVersionUID = 1L;

    public FileException(Integer code, Object[] args) {
        super("file", code, args);
    }

    public FileException(String module, Object[] args) {
        super("file", null, args);
    }
}
