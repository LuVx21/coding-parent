package org.luvx.boot.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {
    private int    code;
    private String msg;
    private T      data;

    public static <T> R<T> of(BizEnum errorCode, String msg, T data) {
        return new R<>(errorCode.getCode(), msg, data);
    }

    public static <T> R<T> success(T data) {
        return R.of(BizEnum.SUCCESS, BizEnum.SUCCESS.getName(), data);
    }

    public static <T> R<T> fail(BizEnum errorCode, T data) {
        return R.of(errorCode, errorCode.getName(), data);
    }

    public static <T> R<T> fail(String msg, T data) {
        return R.of(BizEnum.OPERATION_FAILED, msg, data);
    }

    public static <T> R<T> fail() {
        return R.of(BizEnum.OPERATION_FAILED, BizEnum.OPERATION_FAILED.getName(), null);
    }
}
