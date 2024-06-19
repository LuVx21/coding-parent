package org.luvx.coding.function.lambda;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class MoreLambdas {

    /**
     * 任一元素true
     */
    public static <T> boolean any(Collection<T> c, Predicate<T> filter) {
        for (T t : c) {
            if (filter.test(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 所有元素true
     */
    public static <T> boolean all(Collection<T> c, Predicate<T> filter) {
        for (T t : c) {
            if (!filter.test(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T, R> R reduce(Collection<T> c, R initValue, BiFunction<R, T, R> reducer) {
        R init = initValue;
        for (T t : c) {
            init = reducer.apply(init, t);
        }
        return init;
    }

    public static <T, R> R cast(T t, Function<? super T, ? extends R> mapper) {
        return mapper.apply(t);
    }

    public static <T, K, V> Map<K, V> cast(Collection<T> c,
                                           Function<? super T, ? extends K> keyMapper,
                                           Function<? super T, ? extends V> valueMapper
    ) {
        Map<K, V> map = new HashMap<>(c.size());
        for (T v : c) {
            map.put(keyMapper.apply(v), valueMapper.apply(v));
        }
        return map;
    }

    public static <T> int count(Collection<T> c, Predicate<T> filter) {
        int result = 0;
        for (T t : c) {
            if (filter.test(t)) {
                result += 1;
            }
        }
        return result;
    }

    public static <T> long sum(Collection<T> c, Function<? super T, Long> mapper) {
        long result = 0;
        for (T t : c) {
            result += mapper.apply(t);
        }
        return result;
    }

    /**
     * 根据索引值和值mapper
     */
    public static <T, R> List<R> mapIndex(Collection<T> c, BiFunction<Integer, ? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<R>(c.size());
        int i = 0;
        for (T t : c) {
            result.add(mapper.apply(i, t));
            i++;
        }
        return result;
    }
}
