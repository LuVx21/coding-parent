package org.luvx.coding.common.exception.base;

public interface ResponseCode {
    String getCode();

    String getMessage();

    default BaseException exception() {
        return exception(getMessage());
    }

    default BaseException exception(Throwable cause) {
        return exception(getMessage(), cause);
    }

    BaseException exception(String msg, Object... args);

    BaseException exception(String msg, Throwable cause, Object... args);
}