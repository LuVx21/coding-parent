package org.luvx.coding.common.enums.primitives;

import org.luvx.coding.common.enums.EnumHasCode;

public interface EnumHasIntCode extends EnumHasCode<Integer> {
    int code();

    @Override
    default Integer getCode() {
        return code();
    }
}
