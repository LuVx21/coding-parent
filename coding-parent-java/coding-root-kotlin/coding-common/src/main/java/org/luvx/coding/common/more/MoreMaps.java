package org.luvx.coding.common.more;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Map;

import static org.apache.commons.lang3.ArrayUtils.getLength;

public class MoreMaps {
    public static <K, V> Map<K, V> of(K[] ks, V... vs) {
        ks = Arrays.stream(ks).distinct().toArray(l -> (K[]) new Object[l]);
        if (getLength(ks) != getLength(vs)) {
            throw new IllegalArgumentException("k存在重复或k-v数量不匹配");
        }
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        for (int i = 0; i < ks.length; i++) {
            builder.put(ks[i], vs[i]);
        }
        return builder.build();
    }
}
