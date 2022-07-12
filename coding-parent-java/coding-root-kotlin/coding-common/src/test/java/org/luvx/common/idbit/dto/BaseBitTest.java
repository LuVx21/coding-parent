package org.luvx.common.idbit.dto;

import org.junit.jupiter.api.Test;
import org.luvx.common.idbit.constant.UserClickBitType;
import org.luvx.common.more.MorePrints;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

class BaseBitTest {
    @SneakyThrows
    @Test
    void m1() {
        ObjectMapper objectMapper = new ObjectMapper();

        long value = 0b101;
        UserClickBit bit = new UserClickBit(10000, HasBitInfo.toSet(value, UserClickBitType.class));
        MorePrints.println(
                value,
                objectMapper.writeValueAsString(bit)
        );
    }
}