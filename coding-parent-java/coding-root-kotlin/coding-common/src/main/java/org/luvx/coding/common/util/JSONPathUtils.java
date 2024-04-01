package org.luvx.coding.common.util;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.time.Duration;

public class JSONPathUtils {
    private static final String PREFIX = "$.";

    private static final LoadingCache<String, JSONPath> cache = Caffeine.newBuilder()
            .maximumSize(500)
            .expireAfterAccess(Duration.ofDays(1))
            .removalListener((k, v, e) -> {
            })
            .recordStats()
            .build(new CacheLoader<>() {
                @Override
                public JSONPath load(String key) {
                    return JSONPath.of(key);
                }
            });

    public static JSONPath getJsonPath(String path) {
        JSONPath jsonPath;
        try {
            jsonPath = cache.get(path);
        } catch (Exception e) {
            jsonPath = JSONPath.of(path);
            cache.put(path, jsonPath);
        }
        return jsonPath;
    }

    public static Object get(String jsonStr, String path) {
        return getJsonPath(path).extract(JSONReader.of(jsonStr));
    }

    public static Object get(Object bean, String path) {
        return getJsonPath(path).eval(bean);
    }
}
