package org.luvx.coding.common.exception.base;

import org.apache.commons.lang3.ArrayUtils;
import org.luvx.coding.common.exception.code.CommonResponseCode;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.google.common.base.Strings.lenientFormat;

@Getter
@NoArgsConstructor
public abstract class BaseException extends RuntimeException {
    private ResponseCode responseCode = CommonResponseCode.FAILED;

    public BaseException(String msg, Object... args) {
        super(formatMsg(msg, args));
    }

    public BaseException(String msg, Throwable cause, Object... args) {
        super(formatMsg(msg, args), cause);
    }

    public BaseException(ResponseCode exceptionCode, String msg, Object... args) {
        this(msg, args);
        this.responseCode = exceptionCode;
    }

    public BaseException(ResponseCode exceptionCode, String msg, Throwable cause, Object... args) {
        this(msg, cause, args);
        this.responseCode = exceptionCode;
    }

    private static String formatMsg(String msg, Object[] args) {
        return ArrayUtils.isEmpty(args) ? msg : lenientFormat(msg, args);
    }
}
