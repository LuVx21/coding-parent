package org.luvx.common.idbit.constant;

import static org.luvx.common.idbit.constant.UserClickBitType.E1;
import static org.luvx.common.idbit.constant.UserClickBitType.E2;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.luvx.common.idbit.dto.HasBitInfo;
import org.luvx.common.more.MorePrints;

class HasBitInfoTest {
    @Test
    void m1() {
        Set<UserClickBitType> set = Set.of(E1, E2, UserClickBitType.E62, UserClickBitType.E63);

        long l = HasBitInfo.toLong(set);

        MorePrints.println(
                l,
                Long.toBinaryString(l),
                HasBitInfo.toSet(l, UserClickBitType.class)
        );
    }
}