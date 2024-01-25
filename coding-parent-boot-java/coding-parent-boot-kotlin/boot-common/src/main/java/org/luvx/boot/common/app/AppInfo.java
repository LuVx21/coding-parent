package org.luvx.boot.common.app;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.luvx.coding.common.util.StringUtilsV2;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Optional;

import static com.github.phantomthief.util.MoreFunctions.runCatching;
import static org.luvx.boot.common.util.ApplicationContextUtils.getBeanNullable;

@Slf4j
@Configuration
public class AppInfo implements SmartLifecycle {

    @Override
    public void start() {
        runCatching(() -> {
            String repeat = "-".repeat(80);
            log.info(repeat);
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
            log.info(repeat);
        });
    }

    private String delete(String s) {
        return StringUtilsV2.replace(s, s.lastIndexOf(':', s.indexOf('@')) + 1, s.indexOf('@') - 1, "****");
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    public Integer getPort() {
        return getBeanNullable(Environment.class)
                .map(e -> e.getProperty("server.port"))
                .map(NumberUtils::toInt)
                .orElse(-1);
    }

    public static Optional<AppInfo> instance() {
        return getBeanNullable(AppInfo.class);
    }
}
