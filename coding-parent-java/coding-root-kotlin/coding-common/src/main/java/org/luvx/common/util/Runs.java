package org.luvx.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Runs {
    public static void run(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable ignore) {
        }
    }

    public static void runWithTime(Runnable runnable) {
        long start = System.currentTimeMillis();
        run(runnable);
        long end = System.currentTimeMillis();
        log.info("执行时间:{}ms", end - start);
    }
}
