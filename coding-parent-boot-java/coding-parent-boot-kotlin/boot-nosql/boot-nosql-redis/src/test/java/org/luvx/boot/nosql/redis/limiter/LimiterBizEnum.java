package org.luvx.boot.nosql.redis.limiter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import jakarta.annotation.Nonnull;
import java.time.Duration;

@Getter
@ToString
@AllArgsConstructor
public enum LimiterBizEnum implements FrequencyLimitable {
    NOTIFY("notify", 1, 0, 5, Duration.ofDays(1)),
    ;

    public static final String keyPrefix = "biz_notify";

    private final String   key;
    private final int      down;
    private final long     min;
    private final long     max;
    private final Duration exp;

    @Nonnull
    @Override
    public FrequencyLimiter getLimiter() {
        return new FrequencyLimiter(keyPrefix);
    }
}

