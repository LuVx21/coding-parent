package org.luvx.coding.common.retryer;

import com.github.phantomthief.util.ThrowableRunnable;
import com.github.phantomthief.util.ThrowableSupplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.function.Predicate;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Slf4j
public class RetryUtils {

    private RetryUtils() {
        throw new UnsupportedOperationException();
    }

    public static <X extends Throwable> void runWithRetry(
            ThrowableRunnable<X> func,
            @Nullable Predicate<Throwable> exceptionChecker,
            int maxRetryTimes, Duration retryPeriod
    ) throws X {
        ThrowableSupplier<Object, X> supplier = () -> {
            func.run();
            return null;
        };
        supplyWithRetry(null, supplier, exceptionChecker, maxRetryTimes, retryPeriod);
    }

    public static <T, X extends Throwable> T supplyWithRetry(
            ThrowableSupplier<T, X> func,
            @Nullable Predicate<Throwable> exceptionChecker,
            int maxRetryTimes, Duration retryPeriod
    ) throws X {
        return supplyWithRetry(null, func, exceptionChecker, maxRetryTimes, retryPeriod);
    }

    public static <T, X extends Throwable> T supplyWithRetry(
            @Nullable String name,
            ThrowableSupplier<T, X> func,
            @Nullable Predicate<Throwable> exceptionChecker,
            int maxRetryTimes, Duration retryPeriod
    ) throws X {
        name = StringUtils.isBlank(name) ? STR."重试\{randomAlphabetic(4)}" : name;
        int times = -1;
        Throwable lastThrowable;
        do {
            try {
                return func.get();
            } catch (Throwable e) {
                if (ObjectUtils.defaultIfNull(exceptionChecker, throwable -> true).negate().test(e)) {
                    throw e;
                }
                if (e instanceof Error) {
                    log.error("重试时可忽略异常", e);
                }
                if (retryPeriod != null && retryPeriod.isPositive()) {
                    sleepUninterruptibly(retryPeriod);
                }
                times++;
                if (times <= maxRetryTimes) {
                    log.warn("{}->当前异常:{}, 当前重试次数:{}", name, e, times);
                }
                lastThrowable = e;
            }
        } while (times <= maxRetryTimes);
        throw (X) lastThrowable;
    }
}
