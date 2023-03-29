package org.luvx.coding.common.exception.base;

import org.luvx.coding.common.exception.BizException;

public interface BizResponseCode extends ResponseCode {
    @Override
    default BaseException exception(Object... args) {
        String msg = String.format(getMessage(), args);
        return new BizException(this, msg, args);
    }

    @Override
    default BaseException exception(Throwable t, Object... args) {
        String msg = String.format(getMessage(), args);
        return new BizException(this, msg, t, args);
    }
}