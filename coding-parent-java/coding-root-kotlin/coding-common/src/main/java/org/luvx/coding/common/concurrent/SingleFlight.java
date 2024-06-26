package org.luvx.coding.common.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * 适用于解决缓存穿透
 */
public class SingleFlight {
    private final ConcurrentMap<Object, Call<?>> calls = new ConcurrentHashMap<>();

    public void run(Object key, Runnable runnable) {
        Supplier<Object> c = () -> {
            runnable.run();
            return null;
        };
        execute(key, c);
    }

    /**
     * 并发执行同一个key的方法是, 实际只执行一次, 并共享执行结果
     * 只有并发时共享结果, 串行调用违背了此方法的使用场景
     *
     * @param key      方法调用时的唯一id,
     *                 因用作于map的key,需要实现{@link Object#hashCode()} 和 {@link Object#equals(Object)}方法
     * @param supplier 具体执行的方法
     * @return 方法返回的结果
     */
    @SuppressWarnings("unchecked")
    public <V> V execute(Object key, Supplier<V> supplier) {
        Call<V> call = (Call<V>) calls.get(key);
        if (call == null) {
            call = new Call<>();
            Call<V> other = (Call<V>) calls.putIfAbsent(key, call);
            if (other == null) {
                try {
                    return call.exec(supplier);
                } finally {
                    calls.remove(key);
                }
            } else {
                call = other;
            }
        }

        try {
            return call.await();
        } catch (Exception e) {
            throw new RuntimeException("single flight异常:", e);
        }
    }

    private static class Call<V> {
        private final Object    lock = new Object();
        private       boolean   finished;
        private       V         result;
        private       Exception exc;

        void finished(V result, Exception exc) {
            synchronized (lock) {
                this.finished = true;
                this.result = result;
                this.exc = exc;
                lock.notifyAll();
            }
        }

        V await() throws Exception {
            synchronized (lock) {
                while (!finished) {
                    lock.wait();
                }
                if (exc != null) {
                    throw exc;
                }
                return result;
            }
        }

        V exec(Supplier<V> supplier) {
            V result = null;
            Exception exc = null;
            try {
                result = supplier.get();
                return result;
            } catch (Exception e) {
                exc = e;
                throw e;
            } finally {
                finished(result, exc);
            }
        }
    }
}