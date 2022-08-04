package org.luvx.common.more;

import io.vavr.collection.Iterator;

public class MorePrints {
    public static void println(Object... objs) {
        System.out.println(
                Iterator.of(objs)
                        .map(String::valueOf)
                        .mkString("\n")
        );
    }
}
