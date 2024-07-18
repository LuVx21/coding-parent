package org.luvx.boot.common.listener.base;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 继承此类, 用于约束事件类型和事件的数据
 *
 * @param <E> 事件类型
 * @param <T> 事件数据
 */
@Getter
public abstract class BaseEvent<E extends Enum<E>, T
        // extends BaseEventData
        >
        extends ApplicationEvent {
    protected E type;
    protected T data;

    public BaseEvent(E type, T data) {
        super(data);
        this.type = type;
        this.data = data;
    }
}