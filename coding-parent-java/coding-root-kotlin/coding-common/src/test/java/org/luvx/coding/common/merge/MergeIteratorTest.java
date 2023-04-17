package org.luvx.coding.common.merge;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

class MergeIteratorTest {
    @Test
    void m1() {
        BiTransformIterator<String, String, String> biTransformIterator = BiTransformIterator.create(
                List.of("a", "b").iterator(),
                List.of("c", "d").iterator(),
                (s1, s2) -> s1 + "_" + s2
        );
        biTransformIterator.forEachRemaining(System.out::println);
    }

    @Test
    void m2() {
        MergeIterator<String> iterator = MergeIterator.create(Comparator.naturalOrder(),
                List.of(
                        List.of("c", "d").iterator(),
                        List.of("a", "b").iterator()
                )
        );
        iterator.forEachRemaining(System.out::println);
    }

    @Test
    void m3() {
        SortedMergeIterator<String, String> iterator = SortedMergeIterator.create(
                List.of("a", "b").iterator(),
                List.of("c", "d").iterator(),
                Comparator.naturalOrder(),
                (s1, s2) -> s1 + "_" + s2
        );
        iterator.forEachRemaining(System.out::println);
    }
}