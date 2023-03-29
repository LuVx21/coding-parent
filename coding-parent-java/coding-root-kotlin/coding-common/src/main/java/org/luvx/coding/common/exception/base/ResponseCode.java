package org.luvx.coding.common.exception.base;

public interface ResponseCode {
    String getCode();

    String getMessage();

    BaseException exception(Object... args);

    BaseException exception(Throwable t, Object... args);
}