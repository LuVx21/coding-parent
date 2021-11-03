package org.luvx.common.util;

import static org.apache.commons.collections4.CollectionUtils.size;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Iterators;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class CursorIteratorExTest {
    @Test
    public void m2_1() {
        final int limit = 10;
        CursorIteratorEx<Item, Long, List<Item>> cursorIterator = CursorIteratorEx
                .<Item, Long, List<Item>> newBuilder()
                .withInitCursor(0L)
                .withDataRetriever((Long cursor) -> dao1(cursor, limit))
                .withDataExtractor((Function<List<Item>, Iterator<Item>>) List::iterator)
                .withCursorExtractor((List<Item> list) -> {
                            if (size(list) < limit) {
                                return null;
                            }
                            Item last = list.get(list.size() - 1);
                            return last.id + 1;
                        }
                )
                .withEndChecker(l -> l == null || NumberUtils.toLong(l.toString()) > 47)
                .build();

        Iterators.partition(cursorIterator.iterator(), 5).forEachRemaining(System.out::println);
    }


    private List<Item> dao1(long cursor, int limit) {
        List<Item> data = LongStream.rangeClosed(0, 112L)
                .filter(l -> l >= cursor)
                .mapToObj(l -> new Item(l, "No" + l))
                .limit(limit)
                .collect(Collectors.toList());
        log.info("cursor:{} limit:{} data:{}", cursor, limit, data);
        return data;
    }

    @AllArgsConstructor
    static class Item {
        long id;
        String name;

        @Override
        public String toString() {
            return ">" + id;
        }
    }
}