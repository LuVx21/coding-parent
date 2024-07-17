package org.luvx.boot.mq.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 可批量消费, 可重试
 */
public abstract class A<K, V> extends KafkaConsumer<K, V> {

    public A(Map<String, Object> configs) {
        super(configs);
    }

    public A(Properties properties) {
        super(properties);
    }

    public A(Properties properties, Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
        super(properties, keyDeserializer, valueDeserializer);
    }

    public A(Map<String, Object> configs, Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
        super(configs, keyDeserializer, valueDeserializer);
    }

    abstract void consume(List<ConsumerRecord<?, ?>> messages);

    @Nonnull
    RetryStrategy<V> retryStrategy() {
        return RetryStrategy.NoneRetryStrategy.getInstance();
    }

}
