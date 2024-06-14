package org.luvx.coding.common;

public class Cmp {
    public static boolean between(long a, long start, long end) {
        return start < a && a < end;
    }

    public static boolean betweenAnd(long a, long start, long end) {
        return start <= a && a <= end;
    }
}
