package org.luvx.boot.nosql.redis.accessor;

/**
 * @param <K> key的实际数据类型, redis中存为string
 */
public interface RedisStringAccessor<K, V> extends BaseRedisAccessor<K, V> {

}
