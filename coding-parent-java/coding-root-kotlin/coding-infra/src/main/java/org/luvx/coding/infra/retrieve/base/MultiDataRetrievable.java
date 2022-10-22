package org.luvx.coding.infra.retrieve.base;

import static java.util.Collections.emptyMap;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.AllArgsConstructor;

/**
 * 批量获取数据
 */
public interface MultiDataRetrievable<K, V> {
    default Map<K, V> get(Collection<K> keys) {
        return emptyMap();
    }

    default void set(Map<K, V> dataMap) {
    }

    static <K, V> MultiDataRetrievable<K, V> getOnly(Function<Collection<K>, Map<K, V>> fun) {
        return new MultiDataRetrievable<>() {
            @Override
            public Map<K, V> get(Collection<K> keys) {
                return fun.apply(keys);
            }
        };
    }

    static <K, V> MultiDataRetrievable<K, V> setOnly(Consumer<Map<K, V>> func) {
        return new MultiDataRetrievable<>() {
            @Override
            public void set(Map<K, V> dataMap) {
                func.accept(dataMap);
            }
        };
    }

    static <K, V> IMultiDataAccessBuilder<K, V> builder(Function<Collection<K>, Map<K, V>> fun) {
        return new IMultiDataAccessBuilder<>(fun);
    }

    @AllArgsConstructor
    class IMultiDataAccessBuilder<K, V> {
        private final Function<Collection<K>, Map<K, V>> getFunc;

        public MultiDataRetrievable<K, V> set(Consumer<Map<K, V>> setFunc) {
            return new MultiDataRetrievable<>() {
                @Override
                public Map<K, V> get(Collection<K> keys) {
                    return getFunc.apply(keys);
                }

                @Override
                public void set(Map<K, V> dataMap) {
                    setFunc.accept(dataMap);
                }
            };
        }
    }
}
