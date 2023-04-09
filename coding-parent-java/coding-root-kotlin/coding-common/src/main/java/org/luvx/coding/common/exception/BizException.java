package org.luvx.coding.common.exception;

import org.luvx.coding.common.exception.base.BaseException;
import org.luvx.coding.common.exception.base.ResponseCode;

public class BizException extends BaseException {
    public BizException() {
        super();
    }

    public BizException(String msg, Object... args) {
        super(msg, args);
    }

    public BizException(String msg, Throwable cause, Object... args) {
        super(msg, cause, args);
    }

    public BizException(ResponseCode exceptionCode, String msg, Object... args) {
        super(exceptionCode, msg, args);
    }

    public BizException(ResponseCode exceptionCode, String msg, Throwable cause, Object... args) {
        super(exceptionCode, msg, cause, args);
    }
}