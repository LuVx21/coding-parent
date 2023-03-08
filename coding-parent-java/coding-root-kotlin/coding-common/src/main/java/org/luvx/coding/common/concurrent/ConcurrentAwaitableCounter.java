package org.luvx.coding.common.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

public final class ConcurrentAwaitableCounter {
    private static final long MAX_COUNT = Long.MAX_VALUE;

    private final Sync sync = new Sync();

    public static long nextCount(long prevCount) {
        return (prevCount + 1) & MAX_COUNT;
    }

    public void increment() {
        sync.releaseShared(1);
    }

    public void awaitCount(long totalCount) throws InterruptedException {
        checkTotalCount(totalCount);
        long currentCount = sync.getCount();
        while (compareCounts(totalCount, currentCount) > 0) {
            sync.acquireSharedInterruptibly(currentCount);
            currentCount = sync.getCount();
        }
    }

    private static void checkTotalCount(long totalCount) {
        if (totalCount < 0) {
            throw new AssertionError(
                    "Total count must always be >= 0, even in the face of overflow. "
                            + "The next count should always be obtained by calling ConcurrentAwaitableCounter"
                            + ".nextCount(prevCount), "
                            + "not just +1"
            );
        }
    }

    public void awaitCount(long totalCount, long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        checkTotalCount(totalCount);
        long nanos = unit.toNanos(timeout);
        long currentCount = sync.getCount();
        while (compareCounts(totalCount, currentCount) > 0) {
            if (!sync.tryAcquireSharedNanos(currentCount, nanos)) {
                throw new TimeoutException();
            }
            currentCount = sync.getCount();
        }
    }

    private static int compareCounts(long count1, long count2) {
        long diff = (count1 - count2) & MAX_COUNT;
        if (diff == 0) {
            return 0;
        }
        return diff < MAX_COUNT / 2 ? 1 : -1;
    }

    public void awaitNextIncrements(long nextIncrements) throws InterruptedException {
        if (nextIncrements <= 0) {
            throw new IllegalArgumentException("nextIncrements is not positive: " + nextIncrements);
        }
        if (nextIncrements > MAX_COUNT / 4) {
            throw new UnsupportedOperationException("Couldn't wait for so many increments: " + nextIncrements);
        }
        awaitCount((sync.getCount() + nextIncrements) & MAX_COUNT);
    }

    public boolean awaitFirstIncrement(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(0, unit.toNanos(timeout));
    }

    private static class Sync extends AbstractQueuedLongSynchronizer {
        @Override
        protected long tryAcquireShared(long countWhenWaitStarted) {
            long currentCount = getState();
            return compareCounts(currentCount, countWhenWaitStarted) > 0 ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(long increment) {
            long count;
            long nextCount;
            do {
                count = getState();
                nextCount = (count + increment) & MAX_COUNT;
            } while (!compareAndSetState(count, nextCount));
            return true;
        }

        long getCount() {
            return getState();
        }
    }
}