package org.luvx.boot.common.listener.base;

import org.luvx.coding.common.enums.EnumHasCode;
import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public abstract class BaseEvent<E extends Enum<E> & EnumHasCode<?>, T extends BaseEventData>
        extends ApplicationEvent {
    protected E type;
    protected T data;

    public BaseEvent(E type, T data) {
        super(data);
        this.type = type;
        this.data = data;
    }
}