package org.luvx.coding.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoreArraysTest {

    @Test
    void m1() {
        long[] array = new long[]{10, 3, 8, 9, 9, 4};
        int[] r = MoreArrays.argsort(array);
        int[] is = {1, 5, 2, 3, 3, 0};
        Assertions.assertArrayEquals(is, r);
    }
}