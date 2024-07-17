package org.luvx.boot.nosql.redis.accessor;

import java.util.*;

/**
 * @param <K> key的实际数据类型, redis中存为string
 */
public interface RedisListAccessor<K, V> extends BaseRedisAccessor<K, List<V>> {

}
