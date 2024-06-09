package org.luvx.coding.common;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.io.Closer;

public class CloseableIterators {

    private CloseableIterators() {
        throw new UnsupportedOperationException("不支持创建实例");
    }

    public static <T> CloseableIterator<T> withEmptyBaggage(Iterator<T> innerIterator) {
        return wrap(innerIterator, null);
    }

    public static <T> CloseableIterator<T> concat(List<? extends CloseableIterator<? extends T>> iterators) {
        final Closer closer = Closer.create();
        iterators.forEach(closer::register);

        final Iterator<T> innerIterator = Iterators.concat(iterators.iterator());
        return wrap(innerIterator, closer);
    }

    public static <T> CloseableIterator<T> mergeSorted(List<? extends CloseableIterator<? extends T>> iterators,
            Comparator<T> comparator
    ) {
        Preconditions.checkNotNull(comparator);

        final Closer closer = Closer.create();
        iterators.forEach(closer::register);

        final Iterator<T> innerIterator = Iterators.mergeSorted(iterators, comparator);
        return wrap(innerIterator, closer);
    }

    public static <T> CloseableIterator<T> wrap(@Nonnull Iterator<T> iterator, @Nullable Closeable closeable) {
        return new CloseableIterator<>() {
            private boolean closed;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }

            @Override
            public void close() throws IOException {
                if (!closed) {
                    if (closeable != null) {
                        closeable.close();
                    }
                    closed = true;
                }
            }
        };
    }
}
