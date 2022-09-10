package org.luvx.infra.retrieve.retriever;

import static java.util.function.Function.identity;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.luvx.infra.retrieve.base.MultiDataRetrievable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmptyHolderDataRetriever<K, V> implements MultiDataRetrievable<K, V> {
    private final Supplier<V> emptyHolderSupplier;

    public EmptyHolderDataRetriever(V emptyHolder) {
        this(() -> emptyHolder);
    }

    @Override
    public Map<K, V> get(Collection<K> keys) {
        return keys.stream()
                .collect(Collectors.toMap(identity(), id -> emptyHolderSupplier.get()));
    }
}
