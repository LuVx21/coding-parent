package org.luvx.coding.common.function;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Function;

public class CollectionsF {
    public static <T> Function<Collection<T>, Long> sum(Function<T, Long> convertor) {
        return collection -> {
            var sum = 0L;
            for (T t : collection) {
                sum += convertor.apply(t);
            }
            return sum;
        };
    }

    public static <T> Function<Collection<T>, T> max(Comparator<? super T> convertor) {
        return collection -> Collections.max(collection, convertor);
    }

    public static <T> Function<Collection<T>, T> min(Comparator<? super T> convertor) {
        return collection -> Collections.min(collection, convertor);
    }
}
