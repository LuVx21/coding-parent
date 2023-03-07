package org.luvx.coding.manifold.extensions.java.lang.String;

import org.junit.jupiter.api.Test;

class StringExtTest {
    @Test
    void m1() {
        String s = "a,b,c,d";
        String[] split = s.split(',');
        System.out.println(split.toString());
    }
}