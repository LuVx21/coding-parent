package org.luvx.boot.web.enums;

import org.luvx.coding.common.enums.EnumHasName;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CommonStatusEnum implements EnumHasName<Integer, String> {
    UNKNOWN(0, "未知"),
    /**
     * 无效/非启用等
     */
    INVALID(1, "无效"),
    /**
     * 有效/启用等
     */
    VALID(2, "有效"),
    ;

    private final int    code;
    @Getter
    private final String name;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public boolean isValidBizCode() {
        return this != UNKNOWN;
    }
}