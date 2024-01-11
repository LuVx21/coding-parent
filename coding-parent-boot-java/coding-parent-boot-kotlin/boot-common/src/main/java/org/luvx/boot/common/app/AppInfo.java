package org.luvx.boot.common.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
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
        getBeanNullable(ServerProperties.class).ifPresent((p) ->
                log.info("服务运行在:{}", p.getPort())
        );
        getBeanNullable(DataSourceProperties.class).ifPresent((p) ->
                log.info("数据库服务:{}", p.getUrl())
        );
        getBeanNullable(MongoProperties.class).ifPresent((p) ->
                log.info("MongoDB服务:{}", p.getUri())
        );
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
