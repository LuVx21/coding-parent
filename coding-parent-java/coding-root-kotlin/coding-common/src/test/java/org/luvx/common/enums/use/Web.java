package org.luvx.common.enums.use;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.luvx.common.enums.EnumHasCode;
import org.luvx.common.enums.EnumHasUnknown;

@Getter
@AllArgsConstructor
public enum Web implements EnumHasCode<Integer>, EnumHasUnknown<Web> {
    N(0),
    A(1),
    B(2),
    C(3);
    private final Integer code;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public Web unknown() {
        return N;
    }
}
