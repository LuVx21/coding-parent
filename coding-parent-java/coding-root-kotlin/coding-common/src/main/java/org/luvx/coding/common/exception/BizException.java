package org.luvx.coding.common.exception;

import org.luvx.coding.common.exception.base.BaseException;
import org.luvx.coding.common.exception.base.ResponseCode;

public class BizException extends BaseException {

    public BizException(ResponseCode r, String msg, Object[] args) {
        super(r, msg, args);
    }

    public BizException(ResponseCode r, String msg, Throwable cause, Object[] args) {
        super(r, msg, cause, args);
    }
}