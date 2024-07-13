package org.luvx.boot.nosql.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;

/**
 * @param <T> key的数据类型
 */
public interface BaseRedisAccessor<T> {
    RedisTemplate<String, Object> getRedisTemplate();

    String redisKey(T key);

    default void loadToCache(Collection<T> keys) {
        keys.forEach(k -> {
        });
    }
}
