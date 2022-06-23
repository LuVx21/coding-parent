package org.luvx.common.retryer;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ObjectUtils;

import com.github.phantomthief.util.ThrowableRunnable;
import com.github.phantomthief.util.ThrowableSupplier;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryUtils {

    private RetryUtils() {
        throw new UnsupportedOperationException();
    }

    public static <X extends Throwable> void runWithRetry(int maxRetryTimes, long retryPeriod,
            ThrowableRunnable<X> func, @Nullable Predicate<Throwable> exceptionChecker) throws X {
        supplyWithRetry(maxRetryTimes, retryPeriod, () -> {
            func.run();
            return null;
        }, exceptionChecker);
    }

    public static <T, X extends Throwable> T supplyWithRetry(int maxRetryTimes, long retryPeriod,
            ThrowableSupplier<T, X> func, @Nullable Predicate<Throwable> exceptionChecker) throws X {
        int times = 0;
        Throwable lastThrowable;
        do {
            try {
                return func.get();
            } catch (Throwable e) {
                if (ObjectUtils.defaultIfNull(exceptionChecker, throwable -> true).negate().test(e)) {
                    throw e;
                }
                if (e instanceof Error) {
                    log.error("ignore error in retry", e);
                }
                if (retryPeriod > 0) {
                    sleepUninterruptibly(retryPeriod, MILLISECONDS);
                }
                times++;
                if (times <= maxRetryTimes) {
                    log.warn("try to retry for exception:{}, retry times:{}", e, times);
                }
                lastThrowable = e;
            }
        } while (times <= maxRetryTimes);
        throw (X) lastThrowable;
    }
}
