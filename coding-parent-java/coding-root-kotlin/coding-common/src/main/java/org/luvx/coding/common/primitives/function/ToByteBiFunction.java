package org.luvx.coding.common.primitives.function;

public interface ToByteBiFunction<T, U> {
    byte applyAsByte(T t, U u);
}
