package org.luvx.coding.common.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class OnceTest {
    final Once once = new Once();

    @Test
    void testOnce() throws Exception {
        Runnable runnable = () -> System.out.println("hahaha");

        ThreadUtils.defaultExecutor().execute(() -> once.done(runnable));
        ThreadUtils.defaultExecutor().execute(() -> once.done(runnable));

        TimeUnit.SECONDS.sleep(5);
    }
}