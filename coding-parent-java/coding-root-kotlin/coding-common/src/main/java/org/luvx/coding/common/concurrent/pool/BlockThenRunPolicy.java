package org.luvx.coding.common.concurrent.pool;

import static java.lang.Boolean.FALSE;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class BlockThenRunPolicy implements RejectedExecutionHandler {
    private long              maxBlockingTime;
    private TimeUnit          maxBlockingTimeUnit;
    /**
     * 创建线程池时给到
     */
    private Callable<Boolean> blockingTimeCallback;

    @Override
    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
        BlockingQueue<Runnable> workQueue = executor.getQueue();
        boolean taskSent = false;
        while (!taskSent) {
            if (executor.isShutdown()) {
                throw new RejectedExecutionException(
                        "ThreadPoolExecutor has shutdown while attempting to offer a new task.");
            }

            try {
                if (blockingTimeCallback != null) {
                    if (workQueue.offer(task, maxBlockingTime, maxBlockingTimeUnit)) {
                        startWorkThreadIfNecessary(executor);
                        taskSent = true;
                    } else {
                        Boolean result;
                        try {
                            result = blockingTimeCallback.call();
                        } catch (Exception e) {
                            throw new RejectedExecutionException(e);
                        }
                        if (FALSE.equals(result)) {
                            throw new RejectedExecutionException("User decided to stop waiting for task insertion");
                        }
                    }
                } else {
                    workQueue.put(task);
                    startWorkThreadIfNecessary(executor);
                    taskSent = true;
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void startWorkThreadIfNecessary(ThreadPoolExecutor executor) {
        if (executor.getPoolSize() == 0) {
            executor.prestartCoreThread();
        }
    }
}