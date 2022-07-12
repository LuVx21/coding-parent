package org.luvx.common.idbit.dto;

import org.luvx.common.idbit.constant.UserClickBitType;

import java.util.Set;

public class UserClickBit extends BaseBit<UserClickBitType> {
    public UserClickBit(long id, Set<UserClickBitType> bits) {
        super(id, bits);
    }

    public boolean e0() {
        return getBits().contains(UserClickBitType.E0);
    }

    public boolean e1() {
        return getBits().contains(UserClickBitType.E1);
    }

    public boolean e2() {
        return getBits().contains(UserClickBitType.E2);
    }
}
