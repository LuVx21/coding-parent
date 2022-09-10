package org.luvx.infra.retrieve;

import static java.util.function.Function.identity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.luvx.common.more.MorePrints;
import org.luvx.infra.retrieve.base.MultiDataRetrievable;
import org.luvx.infra.retrieve.exception.AllFailedException;
import org.luvx.infra.retrieve.retriever.LoadingMergerDataRetriever;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class RetrieveIdUtilsTest {
    List<Integer>        ids       = List.of(1, 2, 3, 4, 5);
    Map<Integer, String> cacheData = Map.of(1, "a", 2, "b", 3, "c");
    Map<Integer, String> dbData    = ImmutableMap.<Integer, String> builder()
            .putAll(cacheData).put(4, "d")
            .build();

    private Map<Integer, String> cache(Collection<Integer> ids) {
        Map<Integer, String> data = ids.stream()
                .filter(cacheData::containsKey)
                .collect(Collectors.toMap(identity(), cacheData::get));
        log.info("从cache中获取:{} {}", ids, data);
        return data;
    }

    private Map<Integer, String> db(Collection<Integer> ids) {
        Map<Integer, String> data = ids.stream()
                .filter(dbData::containsKey)
                .collect(Collectors.toMap(identity(), dbData::get));
        log.info("从db中获取:{} {}", ids, data);
        return data;
    }

    @Test
    void m1() {
        Map<Integer, String> newCache = Maps.newHashMap();
        MultiDataRetrievable<Integer, String> cache = new MultiDataRetrievable<>() {
            @Override
            public Map<Integer, String> get(Collection<Integer> keys) {
                return cache(keys);
            }

            @Override
            public void set(Map<Integer, String> dataMap) {
                newCache.putAll(dataMap);
            }
        };
        MultiDataRetrievable<Integer, String> db = new MultiDataRetrievable<>() {
            @Override
            public Map<Integer, String> get(Collection<Integer> keys) {
                return db(keys);
            }
        };


        Map<Integer, String> data = RetrieveIdUtils.get(ids, List.of(cache, db));
        MorePrints.println(newCache, data);
    }

    @Test
    void m2() {
        Map<Integer, String> newCache = Maps.newHashMap();
        MultiDataRetrievable<Integer, String> cache = new MultiDataRetrievable<>() {
            @Override
            public Map<Integer, String> get(Collection<Integer> keys) {
                // return cache(keys);
                throw new RuntimeException();
            }

            @Override
            public void set(Map<Integer, String> dataMap) {
                newCache.putAll(dataMap);
            }
        };
        MultiDataRetrievable<Integer, String> db = new MultiDataRetrievable<>() {
            @Override
            public Map<Integer, String> get(Collection<Integer> keys) {
                return db(keys);
            }
        };

        List<Integer> ids = List.of(1, 2, 3, 4);
        Map<Integer, String> data = RetrieveIdUtils.getFailSafeUnlessAllFailed(ids, List.of(cache, db));
        MorePrints.println(data, newCache);

        Assertions.assertThrows(AllFailedException.class,
                () -> RetrieveIdUtils.getFailSafeUnlessAllFailed(ids, Collections.singletonList(cache)));
    }

    @Test
    void m3() {
        LoadingMergerDataRetriever<Integer, String> db = LoadingMergerDataRetriever.<Integer, String> builder()
                .timeout(10, TimeUnit.MILLISECONDS)
                .loader(this::db)
                .build();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(() -> {
            Map<Integer, String> data = db.get(ids);
            log.info("data1:{}", data);
        });
        executorService.execute(() -> {
            Map<Integer, String> data = db.get(List.of(2, 3, 4));
            log.info("data2:{}", data);
        });
    }

    @Test
    void m4() {
        MultiDataRetrievable<Integer, String> cache = new MultiDataRetrievable<>() {
            final Map<Integer, String> newCache = Maps.newHashMap(cacheData);

            @Override
            public Map<Integer, String> get(Collection<Integer> keys) {
                return ids.stream()
                        .filter(newCache::containsKey)
                        .collect(Collectors.toMap(identity(), newCache::get));
            }

            @Override
            public void set(Map<Integer, String> dataMap) {
                newCache.putAll(dataMap);
                log.info("最新缓存:{}", newCache);
            }
        };

        RetrieveCacheHolder<Integer, String> holder = RetrieveCacheHolder.<Integer, String> builder()
                .cacheRetriever(cache)
                .fromDB(this::db)
                .emptyHolder("空")
                .build();

        Map<Integer, String> data = holder.get(ids);
        log.info("data:{}", data);

        data = holder.get(ids);
        log.info("data:{}", data);
    }
}