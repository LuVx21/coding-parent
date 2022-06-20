package org.luvx.common.concurrent;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadUtils {
    private static final int             size = 5;
    private static       ExecutorService service;

    public static ExecutorService defaultExecutor() {
        return service == null ? (service = getThreadPool()) : service;
    }

    /**
     * 获取线程池
     */
    public static ThreadPoolExecutor getThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
        return new ThreadPoolExecutor(
                size,
                size << 1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    /**
     * 获取Guava封装的线程池
     */
    public static ListeningExecutorService getThreadPool1() {
        return MoreExecutors.listeningDecorator(defaultExecutor());
    }
}
