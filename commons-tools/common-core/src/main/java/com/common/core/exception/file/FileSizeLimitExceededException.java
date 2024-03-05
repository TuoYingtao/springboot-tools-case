package com.common.core.exception.file;

/**
 * 文件名大小限制异常类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-13 10:47
 * @Version: v1.0.0
 */
public class FileSizeLimitExceededException extends FileException {

    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[]{defaultMaxSize});
    }
}
