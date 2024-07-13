package org.luvx.boot.nosql.redis;

import com.google.common.collect.Maps;
import org.springframework.data.redis.connection.RedisHashCommands;
import org.springframework.data.redis.core.RedisCallback;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @param <T> key的数据类型
 */
public interface RedisHashAccessor<T> extends BaseRedisAccessor<T> {
    default Map<T, Map<Object, Object>> getByKeys(Collection<T> keys) {
        List<Object> objects = getRedisTemplate().executePipelined((RedisCallback<Map<byte[], byte[]>>) conn -> {
                    RedisHashCommands redisHashCommands = conn.hashCommands();
                    for (T id : keys) {
                        redisHashCommands.hGetAll(redisKey(id).getBytes());
                    }
                    return null;
                }
        );

        Map<T, Map<Object, Object>> result = Maps.newHashMapWithExpectedSize(keys.size());
        Iterator<T> it = keys.iterator();
        int index = 0;
        while (it.hasNext()) {
            Object o = objects.get(index++);
            Map<Object, Object> map1 = o == null ? Collections.emptyMap() : ((Map<?, ?>) o).entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            result.put(it.next(), map1);
        }
        return result;
    }

    default Map<T, Object> getByHashKeys(Collection<T> keys, Object hashKey) {
        List<Object> objects = getRedisTemplate().executePipelined((RedisCallback<Map<byte[], byte[]>>) conn -> {
                    RedisHashCommands redisHashCommands = conn.hashCommands();
                    for (T id : keys) {
                        redisHashCommands.hGet(redisKey(id).getBytes(), String.valueOf(hashKey).getBytes());
                    }
                    return null;
                }
        );

        Map<T, Object> result = Maps.newHashMapWithExpectedSize(keys.size());
        Iterator<T> it = keys.iterator();
        int index = 0;
        while (it.hasNext()) {
            T next = it.next();
            Object o = objects.get(index++);
            if (o != null) {
                result.put(next, o);
            }
        }
        return result;
    }

    default void setHashBy(T key, Object hashKey, Object hashValue) {
        setHashBy(key, Collections.singletonList(hashKey), hashValue);
    }

    default void setHashBy(T key, Collection<Object> hashKey, Object hashValue) {
        Map<String, Object> map = hashKey.stream()
                .collect(Collectors.toMap(Objects::toString, _ -> hashValue));
        getRedisTemplate().opsForHash().putAll(redisKey(key), map);
    }
}
