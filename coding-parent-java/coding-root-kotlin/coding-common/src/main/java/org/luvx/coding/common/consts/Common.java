package org.luvx.coding.common.consts;

import com.github.phantomthief.util.MoreSuppliers.CloseableSupplier;
import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.time.Duration;
import java.util.concurrent.*;

import static com.github.phantomthief.util.MoreSuppliers.lazy;

public interface Common {
    Splitter SPLITTER_COMMA   = Splitter.on(",");
    Splitter SPLITTER_NEWLINE = Splitter.on("\n");

    CloseableSupplier<RateLimiter> DEFAULT_RATE_LIMITER = lazy(() -> RateLimiter.create(1));

    CloseableSupplier<LoadingCache<String, RateLimiter>> RATE_LIMITER_SUPPLIER = lazy(() -> {
        return CacheBuilder.newBuilder()
                .maximumSize(500)
                .expireAfterAccess(Duration.ofDays(1))
                .recordStats()
                .build(new CacheLoader<>() {
                    @Override
                    public RateLimiter load(String key) {
                        return RateLimiter.create(1);
                    }
                });
    });

    CloseableSupplier<ThreadPoolExecutor> THREAD_POOL_EXECUTOR_SUPPLIER = lazy(() -> {
        final int corePoolSize = 5;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("common-pool-%d").build();
        return new ThreadPoolExecutor(corePoolSize,
                corePoolSize << 1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
    });

    CloseableSupplier<ExecutorService> V_THREAD_POOL_EXECUTOR_SUPPLIER = lazy(() -> {
        ThreadFactory factory = Thread.ofVirtual()
                .name("common-v-pool-", 0)
                .factory();
        return Executors.newThreadPerTaskExecutor(factory);
    });
}
