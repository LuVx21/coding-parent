package org.luvx.coding.infra.retrieve;

import static com.google.common.collect.Maps.newHashMapWithExpectedSize;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singleton;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.luvx.coding.infra.retrieve.base.MultiDataRetrievable;
import org.luvx.coding.infra.retrieve.exception.AllFailedException;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.RateLimiter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RetrieveIdUtils {
    private static final RateLimiter rateLimiter = RateLimiter.create(1);

    private static void rateLog(Runnable doLog) {
        if (rateLimiter.tryAcquire()) {
            doLog.run();
        }
    }

    public static <K, V> V getOne(K key, Iterable<MultiDataRetrievable<K, V>> list) {
        return get(singleton(key), list).get(key);
    }

    /**
     * 多级 cache 带回流, 不缓存 null value.
     *
     * @return map without null value.
     */
    public static <K, V> Map<K, V> get(Collection<K> keys, Iterable<MultiDataRetrievable<K, V>> list) {
        return getByIterator(keys, list.iterator());
    }

    private static <K, V> Map<K, V> getByIterator(Collection<K> keys,
            Iterator<MultiDataRetrievable<K, V>> iterator) {
        if (keys.isEmpty() || !iterator.hasNext()) {
            return emptyMap();
        }

        MultiDataRetrievable<K, V> currentDao = iterator.next();
        Map<K, V> data = currentDao.get(keys);
        Map<K, V> result = newHashMapWithExpectedSize(keys.size());
        Set<K> leftKeys = new HashSet<>();
        for (K key : keys) {
            V value = data.get(key);
            if (value == null) {
                leftKeys.add(key);
            } else {
                result.put(key, value);
            }
        }

        Map<K, V> nextData = getByIterator(leftKeys, iterator);
        if (!nextData.isEmpty()) {
            currentDao.set(nextData);
            result.putAll(nextData);
        }
        return result;
    }

    /**
     * 多级 cache 带回流, 不缓存 null value.
     * 如果有单个 IMultiDataAccess 抛出异常，会 fail-safe 并尝试后续的节点
     *
     * @return map without null value.
     * @throws AllFailedException 如果所有 IMultiDataAccess 都抛出异常，则抛出 AllFailException
     */
    public static <K, V> Map<K, V> getFailSafeUnlessAllFailed(Collection<K> keys,
            Iterable<MultiDataRetrievable<K, V>> list) {
        return getByIteratorFailSafeUnlessAllFailed(Sets.newHashSet(keys), list.iterator(), false);
    }

    private static <K, V> Map<K, V> getByIteratorFailSafeUnlessAllFailed(Set<K> keys,
            Iterator<MultiDataRetrievable<K, V>> iterator, boolean hasSuccess) {
        if (keys.isEmpty()) {
            return emptyMap();
        }
        if (!iterator.hasNext()) {
            if (hasSuccess) {
                return emptyMap();
            } else {
                throw new AllFailedException();
            }
        }

        MultiDataRetrievable<K, V> currentDao = iterator.next();
        Map<K, V> result = newHashMapWithExpectedSize(keys.size());
        try {
            Map<K, V> data = currentDao.get(keys);
            hasSuccess = true;
            data.forEach((k, v) -> {
                if (v != null) {
                    result.put(k, v);
                    keys.remove(k);
                }
            });
        } catch (Throwable t) {
            rateLog(() -> log.warn("[fail-safe] get exception, dao: [{}]", currentDao, t));
        }

        Map<K, V> nextData = getByIteratorFailSafeUnlessAllFailed(keys, iterator, hasSuccess);
        if (!nextData.isEmpty()) {
            try {
                currentDao.set(nextData);
            } catch (Throwable t) {
                rateLog(() -> log.warn("[fail-safe] set exception, dao: [{}]", currentDao, t));
            }
            result.putAll(nextData);
        }
        return result;
    }
}
