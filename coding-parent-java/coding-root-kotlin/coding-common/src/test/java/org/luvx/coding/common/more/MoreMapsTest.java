package org.luvx.coding.common.more;

import java.util.Map;

import org.junit.jupiter.api.Test;

class MoreMapsTest {
    @Test
    void m1() {
        Map<String, String> a = Map.of("a", "b");
        Map<String, String> b = Map.of("b", "c");
        Map<String, String> c = Map.of("c", "d");
        String lookup = MoreMaps.lookup("aa", "foobar", a, b, c);
        System.out.println(lookup);
    }
}