package com.commons.compound.core.asserts;

import com.commons.compound.core.enums.IResponseEnum;
import com.commons.compound.core.exception.BaseException;
import com.commons.compound.core.exception.BusinessException;

import java.text.MessageFormat;

/**
 * 业务异常断言接口
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-06 15:45:40
 * @Version: v1.0.0
*/
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String message = MessageFormat.format(this.getMessage(), args);
        return new BusinessException(this, args, message);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String message = MessageFormat.format(this.getMessage(), args);
        return new BusinessException(this, args, message, t);
    }
}
