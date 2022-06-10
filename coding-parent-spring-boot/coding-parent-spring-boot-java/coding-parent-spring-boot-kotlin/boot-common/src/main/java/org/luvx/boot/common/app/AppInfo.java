package org.luvx.boot.common.app;

import static org.luvx.boot.common.util.ApplicationContextUtils.getBean;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;

import com.github.phantomthief.util.MoreFunctions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AppInfo implements SmartLifecycle {
    @Override
    public void start() {
        MoreFunctions.runCatching(() -> {
            ServerProperties serverProperties = getBean(ServerProperties.class);
            log.info("服务运行在:{}", serverProperties.getPort());
        });
        MoreFunctions.runCatching(() -> {
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
