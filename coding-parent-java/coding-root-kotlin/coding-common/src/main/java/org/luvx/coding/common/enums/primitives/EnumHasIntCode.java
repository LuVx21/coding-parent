package org.luvx.coding.common.enums.primitives;

import org.luvx.coding.common.enums.EnumHasCode;

public interface EnumHasIntCode extends EnumHasCode<Integer> {
    int intCode();

    @Override
    default Integer getCode() {
        return intCode();
    }
}
