package org.luvx.common.retryer;

import javax.annotation.Nullable;

/**
 * Retry的Listener.
 * 用来非侵入的做一些事情, 比如可以用来perf打点..
 * RetryListener的实现不应长时间阻塞执行或抛异常.
 */
public interface RetryListener {
    /**
     * retryer的第retryTimes重试之前执行，带有 retryer 标识
     */
    default void onRetryBegin(@Nullable String name, int retryTimes) {
    }

    /**
     * retryer的第retryTimes重试之后执行，带有 retryer 标识
     */
    default void onRetryEnd(@Nullable String name, int retryTimes) {
    }

    /**
     * 超过最大重试次数以后执行，带有 retryer 标识
     */
    default void onMaxRetryTimesExceeded(@Nullable String name) {
    }
}