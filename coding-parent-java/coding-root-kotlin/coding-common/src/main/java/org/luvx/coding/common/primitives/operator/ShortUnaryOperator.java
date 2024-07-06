package org.luvx.coding.common.primitives.operator;

import static java.util.Objects.requireNonNull;

public interface ShortUnaryOperator {

    short applyAsShort(short operand);

    default ShortUnaryOperator compose(ShortUnaryOperator before) {
        requireNonNull(before);
        return (short v) -> applyAsShort(before.applyAsShort(v));
    }

    default ShortUnaryOperator andThen(ShortUnaryOperator after) {
        requireNonNull(after);
        return (short t) -> after.applyAsShort(applyAsShort(t));
    }

    static ShortUnaryOperator identity() {
        return t -> t;
    }
}
