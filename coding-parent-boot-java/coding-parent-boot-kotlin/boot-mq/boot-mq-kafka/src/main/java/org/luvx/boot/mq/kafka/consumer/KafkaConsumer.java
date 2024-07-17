package org.luvx.boot.mq.kafka.consumer;

import jakarta.annotation.Nonnull;

public interface KafkaConsumer<T> extends CommonKafkaConsumer<T> {
    @Nonnull
    default RetryStrategy<T> retryStrategy() {
        return RetryStrategy.NoneRetryStrategy.getInstance();
    }
}
