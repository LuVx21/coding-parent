package org.luvx.coding.manifold.extensions.java.util.Collection;

import java.util.Collection;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.apache.commons.collections4.CollectionUtils;

@Extension
public class CollectionExt {
    public static boolean isNullOrEmpty(@This Collection<?> coll) {
        return CollectionUtils.isEmpty(coll);
    }
}