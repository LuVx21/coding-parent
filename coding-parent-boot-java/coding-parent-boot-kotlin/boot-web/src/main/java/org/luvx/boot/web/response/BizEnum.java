package org.luvx.boot.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizEnum {
    SUCCESS(10000, "操作成功"),
    OPERATION_FAILED(10001, "系统错误，操作失败"),
    BAD_REQUEST_MSG(40000, "客户端请求参数错误"),
    ;

    private final int    code;
    private final String name;
}
