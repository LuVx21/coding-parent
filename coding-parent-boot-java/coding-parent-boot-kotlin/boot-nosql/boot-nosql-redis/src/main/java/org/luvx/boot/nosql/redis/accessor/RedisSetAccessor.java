package org.luvx.boot.nosql.redis.accessor;

import java.util.Set;

/**
 * @param <K> key的实际数据类型, redis中存为string
 */
public interface RedisSetAccessor<K, V> extends BaseRedisAccessor<K, Set<V>> {

}
