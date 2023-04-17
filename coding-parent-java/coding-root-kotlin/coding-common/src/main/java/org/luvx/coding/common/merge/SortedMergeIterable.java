package org.luvx.coding.common.merge;

import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.BiFunction;

@AllArgsConstructor
public class SortedMergeIterable<T, R> implements Iterable<R> {
    private final Iterable<T>         lhs;
    private final Iterable<T>         rhs;
    private final Comparator<T>       comparator;
    private final BiFunction<T, T, R> fn;

    @Override
    public Iterator<R> iterator() {
        return SortedMergeIterator.create(lhs.iterator(), rhs.iterator(), comparator, fn);
    }

    public static <T, R> SortedMergeIterable<T, R> create(
            Iterable<T> lhs, Iterable<T> rhs, Comparator<T> comparator,
            BiFunction<T, T, R> fn
    ) {
        return new SortedMergeIterable<>(lhs, rhs, comparator, fn);
    }
}
