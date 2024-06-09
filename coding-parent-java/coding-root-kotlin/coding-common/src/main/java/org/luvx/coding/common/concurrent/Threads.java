package org.luvx.coding.common.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Threads {
    public static void sleep(long time) {
        sleep(TimeUnit.MILLISECONDS, time);
    }

    public static void sleep(TimeUnit unit, long time) {
        try {
            unit.sleep(time);
        } catch (Exception ignore) {
        }
    }

    public static void info(String format, Object... args) {
        String x = STR."Time: \{LocalDateTime.now()}, 线程: \{Thread.currentThread().threadId()}-\{Thread.currentThread().getName()}, 数据: \{String.format(format, args)}";
        System.out.println(x);
    }
}
