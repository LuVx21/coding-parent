package org.luvx.coding.common.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

public class Caches {
    public static <K, V> Cache<K, V> createCache() {
        return createCacheBuilder().build();
    }

    public static Caffeine<Object, Object> createCacheBuilder() {
        return Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterAccess(Duration.ofDays(1))
                .recordStats();
    }
}
