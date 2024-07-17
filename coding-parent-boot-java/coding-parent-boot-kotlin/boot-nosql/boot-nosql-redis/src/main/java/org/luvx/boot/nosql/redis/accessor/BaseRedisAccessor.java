package org.luvx.boot.nosql.redis.accessor;

import org.luvx.coding.common.annotation.Immutable;
import org.springframework.data.redis.core.RedisTemplate;

import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

/**
 * @param <K> key的实际数据类型, redis中存为string
 * @param <V> value的类型
 */
public interface BaseRedisAccessor<K, V> {
    RedisTemplate<String, Object> getRedisTemplate();

    /**
     * redis key
     */
    String redisKey(K key);

    /**
     * 放入redis
     */
    void set(K key, V value);

    /**
     * 读取数据
     */
    Map<K, V> loadDataFromDb(Collection<K> ks);

    @Nullable
    default V loadDataFromDb(K k) {
        Map<K, V> m = loadDataFromDb(Collections.singletonList(k));
        if (isEmpty(m)) {
            return null;
        }
        return m.get(k);
    }

    /**
     * 读取数据放入缓存
     */
    @Immutable
    default void loadToCache(Collection<K> keys) {
        Map<K, V> kvMap;
        if (isEmpty(keys) || isEmpty(kvMap = loadDataFromDb(keys))) {
            return;
        }
        kvMap.forEach(this::set);
    }
}
