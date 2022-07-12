package org.luvx.common.retryer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.luvx.common.more.MoreRuns;

import java.time.LocalDateTime;
import java.util.function.Predicate;

@Slf4j
class RetryerTest {
    private static final Retryer<String> retryer = Retryer.<String>newBuilder()
            .name("xxx")
            .maxRetryTimes(5)
            // .retryIfExceptionMatch()
            .retryIfValueMatch(Predicate.isEqual("result").negate())
            // .addRetryListener()
            .retryBackOff(RetryBackOffs.newExponentialBackOff(2_000, 8_000))
            // .waitBeforeRetry(1_000)
            .build();

    @Test
    void m1() {
        MoreRuns.runWithTime(() -> {
            String result = retryer.call(() -> {
                log.info("执行{}", LocalDateTime.now());
                return "result1";
            });
            System.out.println(result);
        });
    }
}