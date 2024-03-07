package org.luvx.boot.common.listener.base;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public abstract class BaseEvent<E extends Enum<E>, T extends BaseEventData>
        extends ApplicationEvent {
    protected E type;
    protected T data;

    public BaseEvent(E type, T data) {
        super(data);
        this.type = type;
        this.data = data;
    }
}