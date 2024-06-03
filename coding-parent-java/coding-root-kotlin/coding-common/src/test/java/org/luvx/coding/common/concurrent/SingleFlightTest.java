package org.luvx.coding.common.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
class SingleFlightTest {

    @Test
    void test01() throws Exception {
        Supplier<String> a = () -> {
            System.out.println("执行...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Throwable ignore) {
            }
            return "a";
        };

        final SingleFlight sf = new SingleFlight();
        Runnable runnable = () -> {
            try {
                String aa = sf.execute("same-key", a);
                log.info("调用:{}-{}", Thread.currentThread().getName(), aa);
            } catch (Exception ignore) {
            }
        };
        Thread.ofVirtual().name("线程1").unstarted(runnable).start();
        Thread.ofVirtual().name("线程2").unstarted(runnable).start();

        TimeUnit.SECONDS.sleep(3);
    }
}