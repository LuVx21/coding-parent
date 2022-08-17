package org.luvx.boot.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {
    private static final String SUCCESS_MSG = "操作成功";
    private static final String ERROR_MSG   = "系统错误，操作失败";

    private int    code;
    private String msg;
    private T      data;

    public static <T> R<T> of(BizEnum errorCode, String message, T data) {
        return new R<>(errorCode.getCode(), message, data);
    }

    public static <T> R<T> success(T data) {
        return R.of(BizEnum.SUCCESS, SUCCESS_MSG, data);
    }

    public static R<String> fail(String message) {
        return R.of(BizEnum.OPERATION_FAILED, message, null);
    }

    public static <T> R<T> fail() {
        return R.of(BizEnum.OPERATION_FAILED, ERROR_MSG, null);
    }
}
