package org.luvx.boot.nosql.redis.limiter;

import jakarta.annotation.Nonnull;
import java.time.Duration;

public interface FrequencyLimitable {
    @Nonnull
    FrequencyLimiter getLimiter();

    String getKey();

    int getDown();

    long getMin();

    long getMax();

    Duration getExp();

    default boolean acquire() {
        FrequencyLimiter limiter = getLimiter();
        return limiter.decr(getKey(), getDown(), getMin(), getMax(), getExp());
    }
}
