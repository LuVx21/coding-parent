package org.luvx.common.util;

import io.vavr.collection.Iterator;

public class PrintUtils {
    public static void println(Object... objs) {
        System.out.println(
                Iterator.of(objs)
                        .map(String::valueOf)
                        .mkString(" ")
        );
    }
}
