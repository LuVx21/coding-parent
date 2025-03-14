package org.luvx.coding.common.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Once {
    private final AtomicInteger done = new AtomicInteger(0);
    private final Lock          lock = new ReentrantLock();

    public void done(Runnable runnable) {
        if (done.get() == 0) {
            doSlow(runnable);
        }
    }

    private void doSlow(Runnable runnable) {
        lock.lock();
        try {
            if (done.get() == 0) {
                runnable.run();
                done.incrementAndGet();
            }
        } finally {
            lock.unlock();
        }
    }
}
