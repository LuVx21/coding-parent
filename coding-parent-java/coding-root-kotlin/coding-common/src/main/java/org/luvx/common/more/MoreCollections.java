package org.luvx.common.more;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.SortedSet;

public class MoreCollections {
    public static <T> Optional<T> first(Collection<T> col) {
        if (col == null || col.isEmpty()) {
            return Optional.empty();
        }
        T first;
        if (col instanceof List) {
            first = ((List<T>) col).get(0);
        } else if (col instanceof SortedSet) {
            first = ((SortedSet<T>) col).first();
        } else if (col instanceof Queue) {
            first = ((Queue<T>) col).poll();
        } else {
            Iterator<T> iterator = col.iterator();
            first = iterator.hasNext() ? iterator.next() : null;
        }
        return Optional.ofNullable(first);
    }
}
