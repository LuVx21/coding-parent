package org.luvx.coding.infra.retrieve.retriever;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.luvx.coding.infra.retrieve.base.MultiDataRetrievable;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

/**
 * 基于缓存的数据提取器
 *
 * @param <K> key类型
 * @param <V> 根据key获取到的数据
 */
@Getter
@RequiredArgsConstructor
public class CacheBasedDataRetriever<K, V> implements MultiDataRetrievable<K, V> {
    private final Cache<K, V> cache;

    public static <K, V> CacheBasedDataRetriever<K, V> of(Cache<K, V> cache) {
        if (cache == null) {
            cache = Caffeine.newBuilder()
                    .maximumSize(500)
                    .expireAfterAccess(Duration.ofDays(1))
                    .recordStats()
                    .build();
        }
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
