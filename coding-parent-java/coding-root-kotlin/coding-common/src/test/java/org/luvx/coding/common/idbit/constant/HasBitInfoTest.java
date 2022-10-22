package org.luvx.coding.common.idbit.constant;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.luvx.coding.common.idbit.dto.HasBitInfo;
import org.luvx.coding.common.more.MorePrints;

class HasBitInfoTest {
    @Test
    void m1() {
        Set<UserClickBitType> set = Set.of(UserClickBitType.E1, UserClickBitType.E2, UserClickBitType.E62, UserClickBitType.E63);

        long l = HasBitInfo.toLong(set);

        MorePrints.println(
                l,
                Long.toBinaryString(l),
                HasBitInfo.toSet(l, UserClickBitType.class)
        );
    }
}