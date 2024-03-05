package com.common.core.exception.file;

/**
 * 文件名称超长限制异常类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-13 10:42
 * @Version: v1.0.0
 */
public class FileNameLengthLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength});
    }
}
