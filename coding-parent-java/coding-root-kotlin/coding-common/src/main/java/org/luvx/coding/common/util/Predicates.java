package org.luvx.coding.common.util;

import java.util.function.Predicate;

public class Predicates {
    public static final Predicate<Throwable> IS_EXCEPTION = input -> input instanceof Exception;
}
