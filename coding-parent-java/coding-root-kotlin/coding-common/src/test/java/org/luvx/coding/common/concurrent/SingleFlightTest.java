package org.luvx.coding.common.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MoreRuns;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
class SingleFlightTest {

    @Test
    void test01() throws Exception {
        Supplier<String> a = () -> {
            Threads.info("执行...");
            Threads.sleep(TimeUnit.SECONDS, 1);

            return "a";
        };

        final SingleFlightV4 sf = new SingleFlightV4();
        // final SingleFlightV2 sf = new SingleFlightV2();
        Runnable runnable = () -> {
            try {
                String aa = sf.execute("same-key", a);
                log.info("调用:{}-{}", Thread.currentThread().getName(), aa);
            } catch (Exception ignore) {
            }
        };
        MoreRuns.runVirtual(runnable, "线程1");
        MoreRuns.runVirtual(runnable, "线程2");
        MoreRuns.runVirtual(runnable, "线程3");

        TimeUnit.SECONDS.sleep(3);
    }
}