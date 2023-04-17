package org.luvx.coding.common.merge;

import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.function.BiFunction;

@AllArgsConstructor
public class BiTransformIterable<T, U, R> implements Iterable<R> {
    private final Iterable<T>         lhs;
    private final Iterable<U>         rhs;
    private final BiFunction<T, U, R> binaryFn;

    @Override
    public Iterator<R> iterator() {
        return BiTransformIterator.create(lhs.iterator(), rhs.iterator(), binaryFn);
    }

    public static <T, U, R> BiTransformIterable<T, U, R> create(Iterable<T> lhs, Iterable<U> rhs, BiFunction<T, U, R> fn) {
        return new BiTransformIterable<>(lhs, rhs, fn);
    }
}
