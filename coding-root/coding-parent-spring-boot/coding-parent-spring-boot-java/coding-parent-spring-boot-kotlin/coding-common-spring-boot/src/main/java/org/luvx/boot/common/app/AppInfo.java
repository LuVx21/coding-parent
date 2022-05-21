package org.luvx.boot.common.app;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;

import static org.luvx.boot.common.util.ApplicationContextUtils.getBean;

@Slf4j
@Configuration
public class AppInfo implements SmartLifecycle {
    @Override
    public void start() {
        Try.runRunnable(() -> {
            ServerProperties serverProperties = getBean(ServerProperties.class);
            log.info("服务运行在:{}", serverProperties.getPort());
        });
        Try.runRunnable(() -> {
            DataSourceProperties dataSourceProperties = getBean(DataSourceProperties.class);
            log.info("数据库服务:{}", dataSourceProperties.getUrl());
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
