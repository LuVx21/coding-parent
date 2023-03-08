package org.luvx.coding.manifold.extensions.java.lang.String;

import java.util.Map;

import com.google.common.collect.Maps;

import org.junit.jupiter.api.Test;

class StringExtTest {
    @Test
    void m1() {
        "a,b,c,d".split(',')
                .toString()
                .println();
    }

    @Test
    void m2() {
        String a = "a";
        a += "b";
        a.println();

        Map<Object, Object> map = Maps.newHashMap()
                .add("a", "b")
                .add(1, 2);
        map.println();
    }
}