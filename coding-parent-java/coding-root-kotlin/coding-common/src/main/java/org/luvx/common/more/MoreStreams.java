package org.luvx.common.more;

import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public class MoreStreams {
    public static <T> Stream<T> toStream(Iterator<T> iterator) {
        Objects.requireNonNull(iterator);
        return stream(spliteratorUnknownSize(iterator, (NONNULL | IMMUTABLE | ORDERED)), false);
    }
}
