package org.luvx.infra.retrieve.retriever;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import org.luvx.infra.retrieve.base.MultiDataRetrievable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class SimpleDbDataRetriever<K, V> implements MultiDataRetrievable<K, V> {
    private final Function<Collection<K>, Map<K, V>> getFromDB;

    @Override
    public Map<K, V> get(Collection<K> keys) {
        return getFromDB.apply(keys);
    }
}