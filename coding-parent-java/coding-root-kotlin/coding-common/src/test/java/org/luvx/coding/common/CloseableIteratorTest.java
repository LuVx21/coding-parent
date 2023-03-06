package org.luvx.coding.common;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.luvx.coding.common.CloseableIterators.concat;
import static org.luvx.coding.common.CloseableIterators.withEmptyBaggage;

class CloseableIteratorTest {
    @Test
    void m1() throws IOException {
        List<CloseableIterator<Integer>> closeableIterators = List.of(
                withEmptyBaggage(List.of(1, 2, 3).iterator()),
                withEmptyBaggage(List.of(4, 5, 6).iterator())
        );

        try (CloseableIterator<Integer> iterator = concat(closeableIterators)) {
            iterator.forEachRemaining(System.out::println);
        }
    }
}