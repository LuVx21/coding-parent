package org.luvx.common.retryer;

/**
 * 创建RetryBackOff的工具类
 */
public final class RetryBackOffs {
    private RetryBackOffs() {
    }

    /**
     * 固定时间的重试
     *
     * @param delayMills 单位是毫秒
     */
    public static RetryBackOff newFixBackOff(long delayMills) {
        return new FixDelayBackOff(delayMills);
    }

    /**
     * 指数退避策略
     *
     * @param initialDelayMillis 起始等待时间
     * @param maxDelayMillis 最大等待时间. 因为指数实在涨的太快，所以需要加一个上限
     */
    public static RetryBackOff newExponentialBackOff(long initialDelayMillis, long maxDelayMillis) {
        return new BinaryExponentialBackOff(initialDelayMillis, maxDelayMillis);
    }

    private static class FixDelayBackOff implements RetryBackOff {
        private final long delay;

        private FixDelayBackOff(long delay) {
            this.delay = checkDelay(delay);
        }

        @Override
        public long nextIntervalOf(int retryTimes, long lastDelayMills, long lastSecondDelayMillis) {
            return delay;
        }
    }

    /**
     * 二进制指数退避实现
     */
    private static class BinaryExponentialBackOff implements RetryBackOff {
        private final long initialDelay;
        private final long maxDelay;

        private BinaryExponentialBackOff(long initialDelay, long maxDelay) {
            this.initialDelay = checkDelay(initialDelay);
            this.maxDelay = checkDelay(maxDelay);
        }

        @Override
        public long nextIntervalOf(int retryTimes, long lastDelayMills, long lastSecondDelayMillis) {
            if (lastDelayMills == 0) {
                return Math.min(initialDelay, maxDelay);
            }
            if (lastDelayMills >= maxDelay) {
                return lastDelayMills;
            }
            return Math.min(lastDelayMills * 2, maxDelay);
        }
    }

    private static long checkDelay(long delayMillis) {
        if (delayMillis < 0) {
            throw new IllegalArgumentException("delay time less than 0: " + delayMillis);
        }
        return delayMillis;
    }
}
