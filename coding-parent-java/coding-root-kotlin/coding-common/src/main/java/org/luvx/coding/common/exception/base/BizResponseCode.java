package org.luvx.coding.common.exception.base;

import org.luvx.coding.common.exception.BizException;

public interface BizResponseCode extends ResponseCode {
    @Override
    default BaseException exception(String msg, Object... args) {
        return new BizException(this, msg, args);
    }

    @Override
    default BaseException exception(String msg, Throwable t, Object... args) {
        return new BizException(this, msg, t, args);
    }
}