package org.luvx.boot.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BizException extends RuntimeException {

    private final String msg;

    public BizException(String msg, Throwable cause) {
        super(cause);
        this.msg = msg;
    }
}
