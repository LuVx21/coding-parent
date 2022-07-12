package org.luvx.common.idbit.dto;

import org.junit.jupiter.api.Test;
import org.luvx.common.idbit.constant.UserClickBitType;
import org.luvx.common.more.MorePrints;

import java.util.Set;

class BaseBitTest {
    @Test
    void m1() {
        Set<UserClickBitType> userClickBitTypes = HasBitInfo.toSet(6, UserClickBitType.class);
        UserClickBit bit = new UserClickBit(10000, userClickBitTypes);
        MorePrints.println(
                bit,
                bit.e0(),
                bit.e1(),
                bit.e2()
        );
    }
}