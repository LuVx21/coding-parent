package org.luvx.common.cursor;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.luvx.common.cursor.CursorIteratorEx;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.apache.commons.collections4.CollectionUtils.size;

@Slf4j
class CursorIteratorExTest {

    @Test
    void m1() {
        final int limit = 10;
        AbstractIterator<List<Item>> iterator = new AbstractIterator<>() {
            private Long cursor = 0L;

            @Override
            protected List<Item> computeNext() {
                List<Item> list = dao1(cursor, limit);
                if (CollectionUtils.isEmpty(list)) {
                    endOfData();
                    return Collections.emptyList();
                }
                Item last = list.get(list.size() - 1);
                cursor = last.id + 1;
                return list;
            }
        };

        iterator.forEachRemaining(System.out::println);
    }

    @Test
    public void m2() {
        final int limit = 10;
        CursorIteratorEx<Item, Long, List<Item>> cursorIterator = CursorIteratorEx
                .<Item, Long, List<Item>>newBuilder()
                .withInitCursor(0L)
                .withDataRetriever((Long cursor) -> dao1(cursor, limit))
                .withDataExtractor(List::iterator)
                .withCursorExtractor((List<Item> list) -> {
                    if (size(list) < limit) {
                        return null;
                    }
                    Item last = list.get(list.size() - 1);
                    return last.id + 1;
                })
                .withEndChecker(l -> l == null || l > 47)
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