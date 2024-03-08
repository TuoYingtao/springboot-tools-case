package com.common.core.asserts;

import com.common.core.enums.IResponseEnum;
import com.common.core.exception.BaseException;
import com.common.core.exception.BusinessAssertException;

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
        return new BusinessAssertException(this, args, message);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String message = MessageFormat.format(this.getMessage(), args);
        return new BusinessAssertException(this, args, message, t);
    }
}
