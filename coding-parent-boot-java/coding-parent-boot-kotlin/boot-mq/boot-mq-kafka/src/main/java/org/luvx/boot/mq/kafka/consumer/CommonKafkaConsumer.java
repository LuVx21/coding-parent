package org.luvx.boot.mq.kafka.consumer;

public interface CommonKafkaConsumer<T> {
    String topic();

    /**
     * 消费组
     * 不同的consumer, 应尽量使用不同的group, 即使是不同的topic
     * 建议: {topic_name}_{consumer_name}
     */
    String consumerGroup();

    /**
     * 反序列化消息
     *
     * @param bytes 待反序列化的消息
     * @return 反序列化后的对象
     */
    default T decode(byte[] bytes) {
        throw new UnsupportedOperationException("TODO: 暂不支持");
    }
}
