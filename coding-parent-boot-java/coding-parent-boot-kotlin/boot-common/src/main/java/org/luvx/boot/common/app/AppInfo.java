package org.luvx.boot.common.app;

import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.common.util.StringUtils;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;

import static org.luvx.boot.common.util.ApplicationContextUtils.getBeanNullable;

@Slf4j
@Configuration
public class AppInfo implements SmartLifecycle {

    @Override
    public void start() {
        getBeanNullable(ServerProperties.class).ifPresent(p ->
                log.info("服务运行在: {}", p.getPort())
        );
        getBeanNullable(DataSourceProperties.class).ifPresent(p ->
                log.info("数据库服务: {}", p.getUrl())
        );
        getBeanNullable(RedisProperties.class).ifPresent(p ->
                log.info("Redis服务: {}", delete(p.getUrl()))
        );
        getBeanNullable(MongoProperties.class).ifPresent(p ->
                log.info("MongoDB服务: {}", delete(p.getUri()))
        );
        getBeanNullable(ElasticsearchProperties.class).ifPresent(p ->
                log.info("Elasticsearch服务: {}", p.getUris())
        );
        getBeanNullable(KafkaProperties.class).ifPresent(p ->
                log.info("Kafka服务: {}", p.getBootstrapServers())
        );
    }

    private String delete(String s) {
        return StringUtils.replace(s, s.lastIndexOf(':', s.indexOf('@')) + 1, s.indexOf('@') - 1, "****");
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
