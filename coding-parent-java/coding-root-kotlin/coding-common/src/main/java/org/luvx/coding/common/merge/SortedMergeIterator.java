package org.luvx.coding.common.merge;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public class SortedMergeIterator<T, R> implements Iterator<R> {

    private final PeekingIterator<T>  lhs;
    private final PeekingIterator<T>  rhs;
    private final Comparator<T>       comparator;
    private final BiFunction<T, T, R> fn;

    public SortedMergeIterator(
            Iterator<T> lhs, Iterator<T> rhs, Comparator<T> comparator,
            BiFunction<T, T, R> fn
    ) {
        this.lhs = Iterators.peekingIterator(lhs);
        this.rhs = Iterators.peekingIterator(rhs);
        this.comparator = comparator;
        this.fn = fn;
    }

    @Override
    public boolean hasNext() {
        return lhs.hasNext() || rhs.hasNext();
    }

    @Override
    public R next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        if (!lhs.hasNext()) {
            return fn.apply(null, rhs.next());
        }
        if (!rhs.hasNext()) {
            return fn.apply(lhs.next(), null);
        }

        int compared = comparator.compare(lhs.peek(), rhs.peek());
        if (compared < 0) {
            return fn.apply(lhs.next(), null);
        }
        if (compared == 0) {
            return fn.apply(lhs.next(), rhs.next());
        }
        return fn.apply(null, rhs.next());
    }

    public static <T, R> SortedMergeIterator<T, R> create(
            Iterator<T> lhs, Iterator<T> rhs, Comparator<T> comparator,
            BiFunction<T, T, R> fn
    ) {
        return new SortedMergeIterator<>(lhs, rhs, comparator, fn);
    }
}
