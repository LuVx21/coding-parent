package org.luvx.coding.infra.retrieve.retriever;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.infra.retrieve.base.MultiDataRetrievable;

import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.function.Function;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@AllArgsConstructor
public class LoadingMergerDataRetriever<K, V> implements MultiDataRetrievable<K, V> {
    /* 毫秒 */
    private final Duration                           waitOtherLoadingTimeout;
    /*  */
    private final Function<Collection<K>, Map<K, V>> loader;

    /* key-holder */
    private final ConcurrentMap<K, LoadingHolder<K, V>> currentLoading = new ConcurrentHashMap<>();

    /**
     * 并发
     *
     * @param keys 获取数据的key
     * @return map结构: k为入参,v为具体的数据
     */
    @Override
    public Map<K, V> get(Collection<K> keys) {
        final CountDownLatch latch = new CountDownLatch(1);
        Map<K, V> result = new HashMap<>();
        Set<K> needLoadKeys = new HashSet<>();
        Map<K, LoadingHolder<K, V>> otherLoading = new HashMap<>();

        Function<K, LoadingHolder<K, V>> f = k -> new LoadingHolder<>(latch, k, result);
        keys.stream().distinct().forEach(key -> {
            LoadingHolder<K, V> holder = currentLoading.computeIfAbsent(key, f);
            if (holder.isCurrent()) {
                needLoadKeys.add(key);
            } else {
                otherLoading.put(key, holder);
            }
        });

        try {
            if (!needLoadKeys.isEmpty()) {
                result.putAll(loader.apply(needLoadKeys));
            }
        } finally {
            needLoadKeys.forEach(currentLoading::remove);
            latch.countDown();
        }

        if (otherLoading.isEmpty()) {
            return result;
        }

        Map<K, V> finalResult = new HashMap<>(result);
        if (waitOtherLoadingTimeout != null && waitOtherLoadingTimeout.isPositive()) {
            long remained = waitOtherLoadingTimeout.toMillis();
            Set<K> reloadKeys = new HashSet<>();
            for (Entry<K, LoadingHolder<K, V>> entry : otherLoading.entrySet()) {
                K key = entry.getKey();
                LoadingHolder<K, V> holder = entry.getValue();
                long now = currentTimeMillis();
                try {
                    V v = holder.get(remained, MILLISECONDS);
                    if (v != null) {
                        finalResult.put(key, v);
                    }
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                } catch (TimeoutException e) {
                    reloadKeys.add(key);
                } catch (Throwable e) {
                    reloadKeys.add(key);
                    log.error("Ops.", e);
                }
                remained -= (currentTimeMillis() - now);
            }
            if (!reloadKeys.isEmpty()) {
                finalResult.putAll(loader.apply(reloadKeys));
            }
        } else {
            otherLoading.forEach((key, holder) -> {
                try {
                    V v = holder.get();
                    if (v != null) {
                        finalResult.put(key, v);
                    }
                } catch (Exception e) {
                    log.error("Ops.", e);
                }
            });
        }
        return finalResult;
    }

    @AllArgsConstructor
    private static final class LoadingHolder<K, V> implements Future<V> {
        private final CountDownLatch latch;
        private final K              key;
        private final Map<K, V>      result;

        private final Thread thread = currentThread();

        boolean isCurrent() {
            return currentThread() == thread;
        }

        @Override
        public boolean isCancelled() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isDone() {
            return latch.getCount() == 0;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V get() throws InterruptedException {
            latch.await();
            return result.get(key);
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
            if (latch.await(timeout, unit)) {
                return result.get(key);
            } else {
                throw new TimeoutException();
            }
        }
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public static final class Builder<K, V> {
        private Duration                           waitOtherLoadingTimeout;
        private Function<Collection<K>, Map<K, V>> loader;

        public Builder<K, V> timeout(long timeout, TimeUnit unit) {
            this.waitOtherLoadingTimeout = Duration.ofMillis(unit.toMillis(timeout));
            return this;
        }

        public Builder<K, V> loader(Function<Collection<K>, Map<K, V>> func) {
            this.loader = func;
            return this;
        }

        public LoadingMergerDataRetriever<K, V> build() {
            requireNonNull(loader);
            return new LoadingMergerDataRetriever<>(waitOtherLoadingTimeout, loader);
        }
    }
}
