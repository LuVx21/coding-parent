package org.luvx.coding.common.idbit;

import org.junit.jupiter.api.Test;
import org.luvx.coding.common.idbit.dto.HasBitInfo;
import org.luvx.coding.common.more.MorePrints;

import java.util.Set;

class HasBitInfoTest {
    @Test
    void m1() {
        var set = Set.of(UserClickBitType.E1, UserClickBitType.E2, UserClickBitType.E62, UserClickBitType.E63);

        long l = HasBitInfo.toLong(set), l1 = HasBitInfo.toLongBit(set);

        MorePrints.println(
                l, l1,
                Long.toBinaryString(l),
                HasBitInfo.toSet(l, UserClickBitType.class)
        );
    }
}