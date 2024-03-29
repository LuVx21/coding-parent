package org.luvx.boot.common.listener.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Slf4j
public abstract class BaseEventListener<EVENT extends BaseEvent<E, T>,
        E extends Enum<E>,
        T extends BaseEventData>
        implements ApplicationListener<EVENT> {

    @Override
    public void onApplicationEvent(EVENT event) {
        E type = event.getType();
        if (!listenEventType().contains(type)) {
            log.info("忽略的事件:{}", type);
            return;
        }
        T data = Objects.requireNonNull(event.getData(), "事件数据为空");
        onEvent(type, data);
    }

    protected Set<E> listenEventType() {
        return Collections.emptySet();
    }

    protected abstract void onEvent(E type, T data);
}
