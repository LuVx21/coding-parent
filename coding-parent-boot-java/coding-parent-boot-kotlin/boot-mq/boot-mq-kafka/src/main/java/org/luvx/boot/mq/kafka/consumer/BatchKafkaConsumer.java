package org.luvx.boot.mq.kafka.consumer;

import java.util.List;

public interface BatchKafkaConsumer<T> extends CommonKafkaConsumer<T> {
    void consume(List<T> messages);
}
