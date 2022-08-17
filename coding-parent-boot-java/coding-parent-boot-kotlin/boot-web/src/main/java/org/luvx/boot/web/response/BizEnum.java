package org.luvx.boot.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizEnum {
    SUCCESS(10000, "SUCCESS"),
    OPERATION_FAILED(10001, "OPERATION_FAILED");

    private final int    code;
    private final String name;
}
