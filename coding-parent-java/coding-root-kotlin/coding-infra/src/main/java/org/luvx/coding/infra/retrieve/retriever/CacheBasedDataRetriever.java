package org.luvx.coding.infra.retrieve.retriever;

import java.util.Collection;
import java.util.Map;

import org.luvx.coding.infra.retrieve.base.MultiDataRetrievable;

import com.google.common.cache.Cache;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CacheBasedDataRetriever<K, V> implements MultiDataRetrievable<K, V> {
    private final Cache<K, V> cache;

    public static <K, V> MultiDataRetrievable<K, V> of(Cache<K, V> cache) {
        return new CacheBasedDataRetriever<>(cache);
    }

    @Override
    public Map<K, V> get(Collection<K> keys) {
        return cache.getAllPresent(keys);
    }

    @Override
    public void set(Map<K, V> dataMap) {
        if (dataMap == null || dataMap.isEmpty()) {
            return;
        }
        dataMap.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .forEach(entry -> cache.put(entry.getKey(), entry.getValue()));
    }
}
