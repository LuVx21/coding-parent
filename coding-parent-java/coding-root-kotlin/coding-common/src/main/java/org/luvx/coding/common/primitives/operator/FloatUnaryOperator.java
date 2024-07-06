package org.luvx.coding.common.primitives.operator;

import static java.util.Objects.requireNonNull;

public interface FloatUnaryOperator {

    float applyAsFloat(float operand);

    default FloatUnaryOperator compose(FloatUnaryOperator before) {
        requireNonNull(before);
        return (float v) -> applyAsFloat(before.applyAsFloat(v));
    }

    default FloatUnaryOperator andThen(FloatUnaryOperator after) {
        requireNonNull(after);
        return (float t) -> after.applyAsFloat(applyAsFloat(t));
    }

    static FloatUnaryOperator identity() {
        return t -> t;
    }
}
