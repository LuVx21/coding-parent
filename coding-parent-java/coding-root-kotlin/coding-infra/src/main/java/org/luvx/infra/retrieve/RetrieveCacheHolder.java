package org.luvx.infra.retrieve;

import static java.util.Collections.singleton;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.luvx.infra.retrieve.base.MultiDataRetrievable;
import org.luvx.infra.retrieve.retriever.EmptyHolderDataRetriever;
import org.luvx.infra.retrieve.retriever.SimpleDbDataRetriever;

import com.google.common.collect.ImmutableList;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于便捷使用缓存
 */
@Slf4j
public final class RetrieveCacheHolder<K, V> {
    private @Nonnull  List<MultiDataRetrievable<K, V>> dataRetrieverList;
    private @Nullable V                                emptyHolder;

    @Nullable
    public V get(K key) {
        return get(singleton(key)).get(key);
    }

    public Map<K, V> get(Collection<K> keys) {
        Map<K, V> rawResult = RetrieveIdUtils.get(keys, dataRetrieverList);
        if (emptyHolder == null) {
            return rawResult;
        }
        // 过滤空占位符
        return rawResult.entrySet().stream()
                .filter(e -> !emptyHolder.equals(e.getValue()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public static final class Builder<K, V> {
        private           boolean                            enableCache;
        private           MultiDataRetrievable<K, V>         cacheRetriever;
        private @Nullable Function<Collection<K>, Map<K, V>> fromDbFunc  = null;
        private @Nullable V                                  emptyHolder = null;

        public Builder<K, V> cacheRetriever(MultiDataRetrievable<K, V> cacheRetriever) {
            this.cacheRetriever = cacheRetriever;
            enableCache = cacheRetriever != null;
            return this;
        }

        public Builder<K, V> enableCache(boolean enableCache) {
            this.enableCache = enableCache;
            return this;
        }

        public Builder<K, V> fromDB(Function<Collection<K>, Map<K, V>> value) {
            this.fromDbFunc = value;
            return this;
        }

        public Builder<K, V> emptyHolder(V emptyHolder) {
            this.emptyHolder = emptyHolder;
            return this;
        }

        public RetrieveCacheHolder<K, V> build() {
            ImmutableList.Builder<MultiDataRetrievable<K, V>> builder = ImmutableList.builder();
            if (enableCache) {
                builder.add(cacheRetriever);
            }
            if (fromDbFunc != null) {
                builder.add(new SimpleDbDataRetriever<>(fromDbFunc));
            }
            if (emptyHolder != null) {
                builder.add(new EmptyHolderDataRetriever<>(emptyHolder));
            }

            RetrieveCacheHolder<K, V> holder = new RetrieveCacheHolder<>();
            holder.dataRetrieverList = builder.build();
            holder.emptyHolder = emptyHolder;
            return holder;
        }
    }
}
