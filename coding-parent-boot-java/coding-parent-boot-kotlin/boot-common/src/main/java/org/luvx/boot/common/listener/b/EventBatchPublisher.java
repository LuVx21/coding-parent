package org.luvx.boot.common.listener.b;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.luvx.boot.common.listener.base.BaseEvent;
import org.luvx.boot.common.util.ApplicationContextUtils;
import org.springframework.context.ApplicationEventPublisher;

public class EventBatchPublisher {
    private List<Pair<BaseEvent<?, ?>, Consumer<BaseEvent<?, ?>>>> list;

    public static EventBatchPublisher of() {
        return new EventBatchPublisher();
    }

    private EventBatchPublisher() {
    }

    public void add(Pair<BaseEvent<?, ?>, Consumer<BaseEvent<?, ?>>> pair) {
        list = ObjectUtils.getIfNull(list, Lists::newLinkedList);
        Objects.requireNonNull(pair.getValue());
        list.add(pair);
    }

    public void add(BaseEvent<?, ?> event, Consumer<BaseEvent<?, ?>> consumer) {
        this.add(Pair.of(event, consumer));
    }

    public EventBatchPublisher add(BaseEvent<?, ?> event) {
        ApplicationEventPublisher publisher = ApplicationContextUtils.getBean(ApplicationEventPublisher.class);
        this.add(Pair.of(event, publisher::publishEvent));
        return this;
    }

    public void publish() {
        for (Pair<BaseEvent<?, ?>, Consumer<BaseEvent<?, ?>>> pair : list) {
            pair.getValue().accept(pair.getKey());
        }
    }
}