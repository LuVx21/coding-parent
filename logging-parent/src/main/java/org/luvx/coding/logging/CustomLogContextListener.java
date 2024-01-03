package org.luvx.coding.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

import java.io.File;
import java.lang.management.ManagementFactory;

public class CustomLogContextListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {

    public static final String LOG_PATH_KEY = "LOG_PATH";

    @Override
    public boolean isResetResistant() {
        return false;
    }

    @Override
    public void onStart(LoggerContext loggerContext) {
    }

    @Override
    public void onReset(LoggerContext loggerContext) {
    }

    @Override
    public void onStop(LoggerContext loggerContext) {
    }

    @Override
    public void onLevelChange(Logger logger, Level level) {
    }

    @Override
    public void start() {
        String projectName = System.getProperty("user.dir");
        File file = new File(projectName);
        String name = file.getName();

        System.setProperty(LOG_PATH_KEY, name);
        Context context = getContext();
        context.putProperty(LOG_PATH_KEY, name);
        context.putProperty("PID", getPid());
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    private String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.substring(0, name.indexOf("@"));
    }
}