package org.luvx.boot.disruptor;

import com.lmax.disruptor.EventHandler;

public interface DisruptorEventConsumer<T> extends EventHandler<T> {
}