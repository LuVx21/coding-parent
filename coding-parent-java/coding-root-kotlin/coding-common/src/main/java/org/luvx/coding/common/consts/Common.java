package org.luvx.coding.common.consts;

import com.github.phantomthief.util.MoreFunctions;
import com.github.phantomthief.util.MoreSuppliers.CloseableSupplier;
import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.*;

import static com.github.phantomthief.util.MoreSuppliers.lazy;

public interface Common {
    Splitter SPLITTER_COMMA   = Splitter.on(",");
    Splitter SPLITTER_NEWLINE = Splitter.on("\n");

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

    default RateLimiter getLimiter(String url) {
        RateLimiter r = MoreFunctions.catching(() -> {
            URI uri = URI.create(url);
            int port = uri.getPort();
            String host = port != -1 ? STR."\{uri.getHost()}:\{port}" : uri.getHost();
            return RATE_LIMITER_SUPPLIER.get().get(host);
        });
        return r != null ? r : RateLimiter.create(1);
    }
}
