package org.luvx.coding.common.merge;

import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

@AllArgsConstructor
public class BiTransformIterator<T, U, R> implements Iterator<R> {
    private final Iterator<T>         lhsIter;
    private final Iterator<U>         rhsIter;
    private final BiFunction<T, U, R> binaryFn;

    @Override
    public boolean hasNext() {
        return lhsIter.hasNext() || rhsIter.hasNext();
    }

    @Override
    public R next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return binaryFn.apply(
                lhsIter.hasNext() ? lhsIter.next() : null,
                rhsIter.hasNext() ? rhsIter.next() : null
        );
    }

    public static <T, U, R> BiTransformIterator<T, U, R> create(
            Iterator<T> lhs, Iterator<U> rhs, BiFunction<T, U, R> fn
    ) {
        return new BiTransformIterator<>(lhs, rhs, fn);
    }
}
