package org.luvx.coding.manifold.extensions.java.util.Map;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.Self;
import manifold.ext.rt.api.This;

import java.util.Map;

@Extension
public class MapExt {
    public static <K, V> @Self Map<K, V> add(@This Map<K, V> map, K key, V value) {
        map.put(key, value);
        return map;
    }
}