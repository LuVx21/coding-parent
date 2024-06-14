package org.luvx.coding.common.function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.luvx.coding.common.function.CollectionsF.*;

class FunctionalTest {

    @Test
    void m1() {
        List<Long> t = List.of(1L, 2L, 3L, 4L, 5L);

        Long sum = sum(Function.identity()).apply(t);
        Long max = max(Comparator.<Long>naturalOrder()).apply(t);
        Long min = min(Comparator.<Long>naturalOrder()).apply(t);

        Assertions.assertEquals(1, min);
        Assertions.assertEquals(5, max);
        Assertions.assertEquals(15, sum);
    }
}