package org.luvx.common.util;

public class Runs {
    public static void run(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable ignore) {
        }
    }
}
