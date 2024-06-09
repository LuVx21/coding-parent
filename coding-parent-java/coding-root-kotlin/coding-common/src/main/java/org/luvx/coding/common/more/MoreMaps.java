package org.luvx.coding.common.more;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import jakarta.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ArrayUtils.getLength;

public class MoreMaps {
    /**
     * @param ks key值
     * @param vs value值
     */
    public static <K, V> Map<K, V> of(K[] ks, V... vs) {
        ks = Arrays.stream(ks).distinct().toArray(l -> (K[]) new Object[l]);
        Preconditions.checkArgument(getLength(ks) == getLength(vs), "k存在重复或k-v数量不匹配");
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        for (int i = 0; i < ks.length; i++) {
            builder.put(ks[i], vs[i]);
        }
        return builder.build();
    }

    public static <K, V> Map<K, V> of(Iterable<K> keys, Iterable<V> values) {
        Map<K, V> result = Maps.newHashMap();

        Iterator<K> keyIter = keys.iterator();
        Iterator<V> valueIter = values.iterator();

        while (keyIter.hasNext()) {
            final K key = keyIter.next();
            if (valueIter.hasNext()) {
                result.put(key, valueIter.next());
            } else {
                break;
            }
        }
        return result;
    }

    public static <K, V> V firstNonNull(Map<? super K, V> map, V defaultValue, K... keys) {
        if (ObjectUtils.anyNull(map, keys)) {
            return defaultValue;
        }
        for (K key : keys) {
            V value = map.get(key);
            if (value != null) {
                return value;
            }
        }
        return defaultValue;
    }

    public static <K, V> List<V> getList(Map<? super K, ?> map, K key) {
        return getList(map, key, null);
    }

    public static <K, V> List<V> getList(Map<? super K, ?> map, K key, @Nullable List<V> defaultValue) {
        Object value;
        if (map == null || (value = map.get(key)) == null) {
            return defaultValue != null ? defaultValue : emptyList();
        }
        Preconditions.checkArgument(value instanceof List, "Key[%s] should be a list, was [%s]", key, value);
        return (List<V>) value;
    }

    /**
     * 前一个map的value作为下一个map的key查询
     */
    @Nullable
    public static <K, V> V lookup(K key, V defaultValue, Map<?, ?>... maps) {
        if (ArrayUtils.isEmpty(maps)) {
            return defaultValue;
        }
        Object k = key;
        for (Map<?, ?> map : maps) {
            k = map.get(k);
            if (k == null) {
                return defaultValue;
            }
        }
        return (V) k;
    }
}
