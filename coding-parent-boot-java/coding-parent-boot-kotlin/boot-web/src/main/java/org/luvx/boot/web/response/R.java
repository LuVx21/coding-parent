package org.luvx.boot.web.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.luvx.coding.common.exception.base.ResponseCode;
import org.luvx.coding.common.exception.code.CommonResponseCode;
import org.luvx.coding.common.net.NetUtils;

import java.io.Serializable;
import java.util.Map;

@Getter
@NoArgsConstructor
public class R<T> implements Serializable {
    private Map<String, String> host = NetUtils.getHostInfo();

    private String code;
    private String msg;
    private T      data;

    public static <T> R<T> success() {
        return success(null);
    }

    public static <T> R<T> success(T data) {
        return of(CommonResponseCode.SUCCESS, data);
    }

    public static <T> R<T> fail() {
        return of(CommonResponseCode.FAILED, null);
    }

    public static <T> R<T> fail(ResponseCode code) {
        return of(code, null);
    }

    public static <T> R<T> fail(ResponseCode code, String msg) {
        return of(code.getCode(), msg, null);
    }

    public static <T> R<T> of(ResponseCode b, T data) {
        return of(b.getCode(), b.getMessage(), data);
    }

    private static <T> R<T> of(String code, String msg, T data) {
        R<T> r = new R<>();
        r.code = code;
        r.msg = msg;
        r.data = data;
        return r;
    }
}
