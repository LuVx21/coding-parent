package org.luvx.coding.common.idbit;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.idbit.dto.BaseBit;
import org.luvx.coding.common.idbit.dto.HasBitInfo;
import org.luvx.coding.common.more.MorePrints;
import org.luvx.coding.common.util.JsonUtils;

class BaseBitTest {
    @SneakyThrows
    @Test
    void m1() {
        long value = 0b101;
        UserClickBit bit = new UserClickBit(value);
        MorePrints.println(
                value,
                bit,
                bit.positive(UserClickBitType.E63),
                JsonUtils.toJson(bit)
        );
    }

    static class UserClickBit extends BaseBit<UserClickBitType> {
        public UserClickBit(long data) {
            super(data, HasBitInfo.toSet(data, UserClickBitType.class));
        }

        public boolean isE0() {
            return positive(UserClickBitType.E0);
        }

        public boolean isE1() {
            return positive(UserClickBitType.E1);
        }

        public boolean isE2() {
            return positive(UserClickBitType.E2);
        }
    }
}