package org.luvx.coding.common.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可暂停的线程池
 */
public class PauseableThreadPoolExecutor extends ThreadPoolExecutor {
    private boolean isPaused;

    private final ReentrantLock lock      = new ReentrantLock();
    private final Condition     condition = lock.newCondition();

    public PauseableThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                       long keepAliveTime,
                                       TimeUnit unit,
                                       BlockingQueue<Runnable> workQueue,
                                       ThreadFactory threadFactory,
                                       RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        lock.lock();
        try {
            while (isPaused) {
                condition.await();
            }
        } catch (InterruptedException e) {
            t.interrupt();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        lock.lock();
        try {
            isPaused = true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 取消暂停
     */
    public void resume() {
        lock.lock();
        try {
            isPaused = false;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}