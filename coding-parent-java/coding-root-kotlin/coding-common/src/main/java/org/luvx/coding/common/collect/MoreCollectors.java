package org.luvx.coding.common.collect;

import com.alibaba.fastjson2.JSONArray;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static org.luvx.coding.common.function.MoreFunctions.castingIdentity;

public class MoreCollectors {

    public static <T> Collector<T, ?, JSONArray> toJsonArray() {
        return new CollectorImpl<>(JSONArray::new, JSONArray::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH)));
    }

    public record CollectorImpl<T, A, R>(Supplier<A> supplier,
                                         BiConsumer<A, T> accumulator,
                                         BinaryOperator<A> combiner,
                                         Function<A, R> finisher,
                                         Set<Characteristics> characteristics
    ) implements Collector<T, A, R> {
        public CollectorImpl(Supplier<A> supplier,
                             BiConsumer<A, T> accumulator,
                             BinaryOperator<A> combiner,
                             Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }
    }
}
