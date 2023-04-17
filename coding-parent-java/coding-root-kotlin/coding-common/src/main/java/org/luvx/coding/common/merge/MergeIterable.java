package org.luvx.coding.common.merge;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
public class MergeIterable<T> implements Iterable<T> {
    private final Comparator<T>         comparator;
    private final Iterable<Iterable<T>> baseIterables;

    @Override
    public Iterator<T> iterator() {
        List<Iterator<T>> iterators = Lists.newArrayList();
        for (Iterable<T> baseIterable : baseIterables) {
            iterators.add(baseIterable.iterator());
        }
        return new MergeIterator<>(comparator, iterators);
    }
}
