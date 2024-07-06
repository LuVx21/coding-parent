package org.luvx.coding.common.primitives.consumer;

public interface ObjByteConsumer<T> {
    void accept(T t, byte value);
}
