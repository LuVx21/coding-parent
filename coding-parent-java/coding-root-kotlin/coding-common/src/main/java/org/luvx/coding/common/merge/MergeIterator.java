package org.luvx.coding.common.merge;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.*;

/**
 * 按照内部元素的优先级合并多个迭代器，
 * 效果等同于对多个迭代器内元素整体排序
 */
public class MergeIterator<T> implements Iterator<T> {
    private final PriorityQueue<PeekingIterator<T>> queue;

    public MergeIterator(final Comparator<T> comparator, List<Iterator<T>> iterators) {
        queue = new PriorityQueue<>(16, (lhs, rhs) -> comparator.compare(lhs.peek(), rhs.peek()));
        for (Iterator<T> iterator : iterators) {
            final PeekingIterator<T> iter = Iterators.peekingIterator(iterator);
            if (iter.hasNext()) {
                queue.add(iter);
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        PeekingIterator<T> pi = queue.remove();
        T t = pi.next();
        if (pi.hasNext()) {
            queue.add(pi);
        }
        return t;
    }

    public static <T> MergeIterator<T> create(Comparator<T> comparator, List<Iterator<T>> iterators) {
        return new MergeIterator<>(comparator, iterators);
    }
}
