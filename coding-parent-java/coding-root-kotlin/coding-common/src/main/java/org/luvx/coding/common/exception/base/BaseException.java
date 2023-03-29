package org.luvx.coding.common.exception.base;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    ResponseCode responseCode;
    Object[]     args;

    public BaseException(ResponseCode exceptionCode, Object... args) {
        this.responseCode = exceptionCode;
        this.args = args;
    }

    public BaseException(ResponseCode exceptionCode, String msg, Object... args) {
        super(msg);
        this.responseCode = exceptionCode;
        this.args = args;
    }

    public BaseException(ResponseCode exceptionCode, String msg, Throwable cause, Object... args) {
        super(msg, cause);
        this.responseCode = exceptionCode;
        this.args = args;
    }
}
