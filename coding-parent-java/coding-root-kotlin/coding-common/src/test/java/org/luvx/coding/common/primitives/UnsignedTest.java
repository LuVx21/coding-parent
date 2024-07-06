package org.luvx.coding.common.primitives;

import com.google.common.primitives.UnsignedInteger;
import org.junit.jupiter.api.Test;

class UnsignedTest {
    @Test
    void m00() {
        short b = Unsigned.unsignedByte(Byte.MAX_VALUE);
        System.out.println(Integer.toBinaryString(b));
        System.out.println(Integer.toBinaryString(b + 1));

        int s = Unsigned.unsignedShort(Short.MAX_VALUE);
        System.out.println(Integer.toBinaryString(s));
        System.out.println(Integer.toBinaryString(s + 1));

        long i = Unsigned.unsignedInt(Integer.MAX_VALUE);
        System.out.println(Long.toBinaryString(i));
        System.out.println(Long.toBinaryString(i + 1));
    }

    @Test
    void m01() {
        UnsignedInteger ui = UnsignedInteger.valueOf(Integer.MAX_VALUE + 1L);
        System.out.println(ui.toString(2));
        UnsignedInteger maxValue = UnsignedInteger.MAX_VALUE;
    }
}