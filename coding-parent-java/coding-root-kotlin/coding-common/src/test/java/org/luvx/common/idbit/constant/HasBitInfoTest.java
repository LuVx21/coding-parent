package org.luvx.common.idbit.constant;

import org.junit.jupiter.api.Test;
import org.luvx.common.idbit.dto.HasBitInfo;
import org.luvx.common.more.MorePrints;

import java.util.Set;

class HasBitInfoTest {
    @Test
    void m1() {
        long l = HasBitInfo.toLong(
                Set.of(UserClickBitType.E0, UserClickBitType.E1, UserClickBitType.E2)
        );
        MorePrints.println(
                UserClickBitType.E2.bitValue(),
                l,
                Long.toBinaryString(l),
                HasBitInfo.toSet(l, UserClickBitType.class),

                // Long.toBinaryString(4),
                HasBitInfo.toSet(7, UserClickBitType.class)
        );
    }
}