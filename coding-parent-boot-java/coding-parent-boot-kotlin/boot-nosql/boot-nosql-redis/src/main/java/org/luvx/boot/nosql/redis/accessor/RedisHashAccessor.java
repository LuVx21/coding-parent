package org.luvx.boot.nosql.redis.accessor;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.luvx.coding.infra.retrieve.RetrieveIdUtils;
import org.luvx.coding.infra.retrieve.base.MultiDataRetrievable;
import org.luvx.coding.infra.retrieve.retriever.SimpleDbDataRetriever;
import org.springframework.data.redis.connection.RedisHashCommands;
import org.springframework.data.redis.core.RedisCallback;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @param <K> key的实际数据类型, redis中存为string
 */
public interface RedisHashAccessor<K, HK, HV> extends BaseRedisAccessor<K, Map<HK, HV>> {

    @Override
    default void set(K key, Map<HK, HV> value) {
        var hashOps = getRedisTemplate().<String, HV>opsForHash();
        Map<String, HV> smap = value.entrySet().stream()
                .collect(Collectors.toMap(e -> Objects.toString(e.getKey()), Map.Entry::getValue));
        hashOps.putAll(redisKey(key), smap);
    }

    default Map<K, Map<HK, HV>> getByKeys(Collection<K> keys) {
        MultiDataRetrievable<K, Map<HK, HV>> fromRedis = new MultiDataRetrievable<>() {
            @Override
            public Map<K, Map<HK, HV>> get(Collection<K> ks) {
                List<Object> objects = getRedisTemplate().executePipelined((RedisCallback<Map<byte[], byte[]>>) conn -> {
                            RedisHashCommands redisHashCommands = conn.hashCommands();
                            for (K id : keys) {
                                redisHashCommands.hGetAll(redisKey(id).getBytes());
                            }
                            return null;
                        }
                );

                Map<K, Map<HK, HV>> result = Maps.newHashMapWithExpectedSize(keys.size());
                Iterator<K> it = keys.iterator();
                int index = 0;
                while (it.hasNext()) {
                    K key = it.next();
                    Object o = objects.get(index++);
                    if (ObjectUtils.isNotEmpty(o)) {
                        result.put(key, (Map<HK, HV>) o);
                    }
                }
                return result;
            }

            @Override
            public void set(Map<K, Map<HK, HV>> dataMap) {
                dataMap.forEach(RedisHashAccessor.this::set);
            }
        };
        MultiDataRetrievable<K, Map<HK, HV>> fromDb = new SimpleDbDataRetriever<>(this::loadDataFromDb);

        return RetrieveIdUtils.get(keys, List.of(fromRedis, fromDb));
    }

    default Map<K, Map<HK, HV>> getByHashKeys(Collection<K> keys, Collection<HK> hashKeys) {
        MultiDataRetrievable<K, Map<HK, HV>> fromRedis = new MultiDataRetrievable<>() {
            @Override
            public Map<K, Map<HK, HV>> get(Collection<K> keys) {
                List<Object> objects = getRedisTemplate().executePipelined((RedisCallback<Map<byte[], byte[]>>) conn -> {
                            RedisHashCommands redisHashCommands = conn.hashCommands();
                            for (K id : keys) {
                                byte[] idb = redisKey(id).getBytes();
                                for (HK hashKey : hashKeys) {
                                    redisHashCommands.hGet(idb, String.valueOf(hashKey).getBytes());
                                }
                            }
                            return null;
                        }
                );

                Map<K, Map<HK, HV>> result = Maps.newHashMapWithExpectedSize(keys.size());
                Iterator<K> it = keys.iterator();
                int index = 0;
                while (it.hasNext()) {
                    K key = it.next();
                    Iterator<HK> iterator = hashKeys.iterator();
                    Map<HK, HV> hkv = Maps.newHashMapWithExpectedSize(hashKeys.size());
                    while (iterator.hasNext()) {
                        HK hk = iterator.next();
                        Object hv = objects.get(index++);
                        if (hv != null) {
                            hkv.put(hk, (HV) hv);
                        }
                    }
                    if (ObjectUtils.isNotEmpty(hkv)) {
                        result.put(key, hkv);
                    }
                }
                return result;
            }

            @Override
            public void set(Map<K, Map<HK, HV>> dataMap) {
                dataMap.forEach(RedisHashAccessor.this::set);
            }
        };

        MultiDataRetrievable<K, Map<HK, HV>> fromDb = new SimpleDbDataRetriever<>(ks -> loadDataFromDb(ks, hashKeys));
        return RetrieveIdUtils.get(keys, List.of(fromRedis, fromDb));
    }

    /**
     * 取出了多余的数据, 建议视情况重写(hks没参与从db过滤数据)
     */
    default Map<K, Map<HK, HV>> loadDataFromDb(Collection<K> ks, Collection<HK> hks) {
        hks = Sets.newHashSet(hks);
        // 这里取出了多余hk的数据
        Map<K, Map<HK, HV>> result = loadDataFromDb(ks);
        for (Map.Entry<K, Map<HK, HV>> entry : result.entrySet()) {
            Map<HK, HV> value = entry.getValue();
            Collection<HK> finalHks = hks;
            value.entrySet().removeIf(e -> !finalHks.contains(e.getKey()));
        }
        return result;
    }

    default void setHashBy(K key, HK hashKey, HV hashValue) {
        setHashBy(key, Collections.singletonList(hashKey), hashValue);
    }

    default void setHashBy(K key, Collection<HK> hashKey, HV hashValue) {
        Map<String, Object> map = hashKey.stream()
                .collect(Collectors.toMap(Objects::toString, _ -> hashValue));
        getRedisTemplate().opsForHash().putAll(redisKey(key), map);
    }
}
