package org.luvx.common.retryer;

import com.github.phantomthief.util.ThrowableRunnable;
import com.github.phantomthief.util.ThrowableSupplier;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * 用来方便执行重试逻辑的工具类。Retryer是immutable的，可以重用。
 * Retryer通过Builder创建，maxRetryTimes 和 retryBackOff策略是强制要求设置的.
 * 比如:
 * <pre>{@code
 *      private static final Retryer<String> retryer = Retryer.<String>newBuilder()
 *                 .name("xxx")
 *                 .maxRetryTimes(5)
 *                 .waitBeforeRetry(100)
 *                 .build();
 *
 *      public void myMethod() {
 *          String result = retryer.call(() -> "result");
 *      }
 * }
 * </pre>
 * <p>
 * Retryer目前的实现尽量保持简单和创建开销，有更多需求的时候再添加功能。如果重试需求非常简单，可以使用{@link RetryUtils}
 *
 * @param <T> 重试代码块的返回值类型. 如果重试的代码块没有返回值，可以使用{@code Retryer<Void> }
 */
@Slf4j
@Immutable
public class Retryer<T> {
    /**
     * 给当前的 retryer 起个名字
     */
    private final String name;

    /**
     * 最大重试次数. 重试次数计算不计入第一次非重试的执行。如果值为0，就并不会重试
     */
    private final int maxRetryTimes;

    // 根据异常和返沪值判断是否要重试。为了逻辑清晰一点分成两个Predicate
    /**
     * 如果抛出了异常，根绝异常决定是否重试
     */
    private final Predicate<Throwable> exceptionPredicate;
    /**
     * 如果没有抛出异常，根据返回值决定是否重试
     */
    private final Predicate<? super T> returnValuePredicate;

    /**
     * BackOff策略
     */
    private final RetryBackOff retryBackOff;

    // listeners
    private final List<RetryListener> retryListeners;

    private Retryer(Builder<T> builder) {
        this.name = builder.name;
        this.maxRetryTimes = builder.maxRetryTimes;
        this.exceptionPredicate = builder.exceptionPredicate;
        this.returnValuePredicate = builder.returnValuePredicate;
        this.retryListeners = ImmutableList.copyOf(builder.retryListeners);
        this.retryBackOff = builder.retryBackOff;
    }

    /**
     * 创建一个Builder实例.
     */
    public static <T> Builder<T> newBuilder() {
        return new Builder<>();
    }

    /**
     * 从现有的Retryer实例创建一个Builder
     */
    public Builder<T> toBuilder() {
        Builder<T> builder = new Builder<>();
        builder.name = this.name;
        builder.maxRetryTimes = this.maxRetryTimes;
        builder.exceptionPredicate = this.exceptionPredicate;
        builder.returnValuePredicate = this.returnValuePredicate;
        builder.retryListeners = new ArrayList<>(this.retryListeners);
        builder.retryBackOff = this.retryBackOff;
        return builder;
    }

    /**
     * 用于方便的执行无返回值的代码
     *
     * @param runnable the code to be run
     * @param <X>      重试代码抛出的异常类型
     */
    public <X extends Throwable> void run(ThrowableRunnable<X> runnable) throws X {
        call(() -> {
            runnable.run();
            return null;
        });
    }


    /**
     * 执行一段代码，如果在达到重试次数前成功，则返回代码的返回值；否则抛出异常.
     *
     * @param supplier the code to be run
     * @param <X>      重试代码抛出的异常类型
     * @return value returned by supplier
     * @throws X 如果超过了最大重试次数，且代码抛出了异常
     */
    @Nullable
    public <X extends Throwable> T call(ThrowableSupplier<T, X> supplier) throws X {
        long lastIntervalMillis = 0, lastSecondIntervalMillis = 0;
        for (int i = 0; i <= maxRetryTimes; i++) {
            notifyRetryBegin(i);
            try {
                T v = supplier.get();
                if (!returnValuePredicate.test(v)) {
                    return v;
                }
            } catch (Throwable e) {
                if (!exceptionPredicate.test(e)) {
                    throw e;
                }
                if (i == maxRetryTimes) {
                    notifyMaxRetryTimesExceeded();
                    throw e;
                }
            } finally {
                notifyRetryEnd(i);
            }

            long intervalMillis = retryBackOff.nextIntervalOf(i + 1, lastIntervalMillis, lastSecondIntervalMillis);
            lastSecondIntervalMillis = lastIntervalMillis;
            lastIntervalMillis = intervalMillis;
            // 保护性检查一下
            if (intervalMillis >= 0) {
                try {
                    Thread.sleep(intervalMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    // 不想抛出checked exception，就这样凑乎吧..估计没人会care的
                    throw new RuntimeException("重试时中断");
                }
            }
        }
        notifyMaxRetryTimesExceeded();
        // throw new RuntimeException("超出最大重试次数");
        log.warn("超出最大重试次数:{}", name);
        return null;
    }

    private void notifyRetryBegin(int retryTimes) {
        if (retryTimes <= 0) {
            return;
        }
        for (RetryListener retryListener : retryListeners) {
            try {
                retryListener.onRetryBegin(name, retryTimes);
            } catch (Throwable e) {
                log.error("invoke retry listener failed", e);
            }
        }
    }

    private void notifyRetryEnd(int retryTimes) {
        if (retryTimes <= 0) {
            return;
        }
        for (RetryListener retryListener : retryListeners) {
            try {
                retryListener.onRetryEnd(name, retryTimes);
            } catch (Throwable e) {
                log.error("invoke retry listener failed", e);
            }
        }
    }

    private void notifyMaxRetryTimesExceeded() {
        for (RetryListener retryListener : retryListeners) {
            try {
                retryListener.onMaxRetryTimesExceeded(name);
            } catch (Throwable e) {
                log.error("invoke retry listener failed", e);
            }
        }
    }

    /**
     * Builder 方法
     */
    public static final class Builder<T> {
        private String               name;
        private int                  maxRetryTimes;
        private Predicate<Throwable> exceptionPredicate   = Predicates.alwaysTrue();
        private Predicate<? super T> returnValuePredicate = Predicates.alwaysFalse();
        private List<RetryListener>  retryListeners       = new ArrayList<>();
        private RetryBackOff         retryBackOff;

        private Builder() {
        }

        /**
         * 给当前的 retryer 起个名字
         */
        public Builder<T> name(String n) {
            this.name = requireNonNull(n);
            return this;
        }

        /**
         * 最大重试次数
         */
        public Builder<T> maxRetryTimes(int times) {
            if (times < 0) {
                throw new IllegalArgumentException("invalid retry times");
            }
            this.maxRetryTimes = times;
            return this;
        }

        /**
         * 根据Exception判断是否需要重试. 默认策略遇到Throwable抛出即需要重试.
         * 当调用是，传入的Throwable总是不为null.
         */
        public Builder<T> retryIfExceptionMatch(@Nonnull Predicate<Throwable> predicate) {
            this.exceptionPredicate = requireNonNull(predicate);
            return this;
        }

        /**
         * 当抛出的异常是是指定的异常类型，或者是指定异常类型的子类时，进行重试。默认所有Throwable类型的异常都会重试.
         * 此方法是{@link #retryIfExceptionMatch(Predicate)}的一个方便调用的方法.
         */
        public Builder<T> retryIfExceptionIs(@Nonnull Class<? extends Throwable> cls) {
            return this.retryIfExceptionMatch(cls::isInstance);
        }

        /**
         * 当返回值满足条件时，进行重试. 默认所有返回值均不重试
         */
        public Builder<T> retryIfValueMatch(@Nonnull Predicate<? super T> predicate) {
            this.returnValuePredicate = requireNonNull(predicate);
            return this;
        }

        /**
         * 添加一个 {@link RetryListener}
         */
        public Builder<T> addRetryListener(@Nonnull RetryListener listener) {
            this.retryListeners.add(requireNonNull(listener));
            return this;
        }

        /**
         * 重试RetryBackOff策略.
         * 可用的策略见{@link RetryBackOffs}
         */
        public Builder<T> retryBackOff(@Nonnull RetryBackOff backOff) {
            this.retryBackOff = requireNonNull(backOff);
            return this;
        }

        /**
         * 包装{@link #retryBackOff(RetryBackOff)} 的方法, 用于方便设定固定时间间隔的retry
         *
         * @param intervalMillis 重试前等待毫秒数
         */
        public Builder<T> waitBeforeRetry(long intervalMillis) {
            return this.retryBackOff(RetryBackOffs.newFixBackOff(intervalMillis));
        }

        public Retryer<T> build() {
            // 先警告一下，后面可能强制命名
            if (StringUtils.isEmpty(name)) {
                log.warn("the retryer name was not found, better have one");
            }
            if (this.maxRetryTimes < 0) {
                throw new RuntimeException("max retry times not set");
            }
            if (this.retryBackOff == null) {
                throw new RuntimeException("backOff not set");
            }
            return new Retryer<>(this);
        }
    }
}
