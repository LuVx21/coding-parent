package org.luvx.coding.common.primitives.operator;

import static java.util.Objects.requireNonNull;

public interface ByteUnaryOperator {

    byte applyAsByte(byte operand);

    default ByteUnaryOperator compose(ByteUnaryOperator before) {
        requireNonNull(before);
        return (byte v) -> applyAsByte(before.applyAsByte(v));
    }

    default ByteUnaryOperator andThen(ByteUnaryOperator after) {
        requireNonNull(after);
        return (byte t) -> after.applyAsByte(applyAsByte(t));
    }

    static ByteUnaryOperator identity() {
        return t -> t;
    }
}
