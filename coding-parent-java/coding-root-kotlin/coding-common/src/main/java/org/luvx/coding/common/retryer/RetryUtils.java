package org.luvx.coding.common.retryer;

import com.github.phantomthief.util.ThrowableRunnable;
import com.github.phantomthief.util.ThrowableSupplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.luvx.coding.common.util.Predicates;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.function.Predicate;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Slf4j
public class RetryUtils {

    private RetryUtils() {
        throw new UnsupportedOperationException();
    }

    public static <X extends Throwable> void runWithRetry(
            @Nullable String name,
            ThrowableRunnable<X> func,
            @Nullable Predicate<Throwable> exceptionChecker,
            int maxRetryTimes, Duration retryPeriod,
            @Nullable Predicate<Throwable> throwLastException
    ) throws X {
        ThrowableSupplier<Object, X> supplier = () -> {
            func.run();
            return null;
        };
        supplyWithRetry(name, supplier, exceptionChecker, maxRetryTimes, retryPeriod, throwLastException);
    }

    public static <T, X extends Throwable> T supplyWithRetry(
            ThrowableSupplier<T, X> func,
            @Nullable Predicate<Throwable> exceptionChecker,
            int maxRetryTimes, Duration retryPeriod
    ) throws X {
        return supplyWithRetry(null, func, exceptionChecker, maxRetryTimes, retryPeriod, null);
    }

    /**
     * 最大重试次数后,仍然异常,忽略的话返回null
     *
     * @param shouldRetry        返回true不抛出异常,进行重试
     * @param throwLastException 返回true抛出异常,false则返回null
     */
    @Nullable
    public static <T, X extends Throwable> T supplyWithRetry(
            @Nullable String name,
            ThrowableSupplier<T, X> func,
            @Nullable Predicate<Throwable> shouldRetry,
            int maxRetryTimes, Duration retryPeriod,
            @Nullable Predicate<Throwable> throwLastException
    ) throws X {
        name = StringUtils.isBlank(name) ? STR."重试\{randomAlphabetic(4)}" : name;
        Predicate<Throwable> alwaysTrue = Predicates.alwaysTrue();

        int times = 0;
        Throwable lastThrowable;
        do {
            try {
                return func.get();
            } catch (Throwable e) {
                if (defaultIfNull(shouldRetry, alwaysTrue).negate().test(e)) {
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
                    log.warn("{}->当前异常:{}, 进行第{}次重试", name, e, times);
                }
                lastThrowable = e;
            }
        } while (times <= maxRetryTimes);

        log.warn("进行{}次重试仍失败,最终异常:", maxRetryTimes, lastThrowable);
        if (defaultIfNull(throwLastException, alwaysTrue).test(lastThrowable)) {
            throw (X) lastThrowable;
        } else {
            return null;
        }
    }
}
