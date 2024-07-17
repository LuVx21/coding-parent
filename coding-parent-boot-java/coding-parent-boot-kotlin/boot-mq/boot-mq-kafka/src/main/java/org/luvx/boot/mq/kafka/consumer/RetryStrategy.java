package org.luvx.boot.mq.kafka.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.annotation.Nonnull;
import java.util.Map;

/**
 * 重试策略
 */
public interface RetryStrategy<T> {
    /**
     * 消费重试逻辑
     *
     * @param retryContext 重试上下文
     * @param runnable     业务的consume逻辑
     */
    void retry(@Nonnull RetryContext<T> retryContext, @Nonnull Runnable runnable,
               @Nonnull Exception exception);

    public class NoneRetryStrategy<T> implements RetryStrategy<T> {
        private static final RetryStrategy<?> INSTANCE = new NoneRetryStrategy<>();

        private NoneRetryStrategy() {
        }

        @Override
        public void retry(@Nonnull RetryContext<T> retryContext, @Nonnull Runnable runnable,
                          @Nonnull Exception exception) {
        }

        @SuppressWarnings("unchecked")
        public static <T> RetryStrategy<T> getInstance() {
            return (RetryStrategy<T>) INSTANCE;
        }
    }

    /**
     * 记录message目前的重试信息
     */
    @Getter
    @AllArgsConstructor
    public class RetryContext<T> {
        private final T                   message;
        private final byte[]              messageBytes;
        private final Map<String, String> header;
    }
}
