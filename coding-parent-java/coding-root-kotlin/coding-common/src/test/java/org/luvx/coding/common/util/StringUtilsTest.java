package org.luvx.coding.common.util;

import org.junit.jupiter.api.Test;

class StringUtilsTest {

    @Test
    void
    replaceTest() {

        String abcdefg = StringUtilsV2.replace(
                "abcdefg", 2, 5, "123456789"
        );
        System.out.println(abcdefg);
    }
}