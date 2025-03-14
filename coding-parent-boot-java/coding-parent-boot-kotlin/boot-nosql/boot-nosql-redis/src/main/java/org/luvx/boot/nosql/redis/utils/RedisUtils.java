package org.luvx.boot.nosql.redis.utils;

public class RedisUtils {
    public static String key(String prefix, String key) {
        return prefix + ":" + key;
    }
}
