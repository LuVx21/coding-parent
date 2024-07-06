package org.luvx.coding.common.primitives.operator;

import static java.util.Objects.requireNonNull;

public interface CharUnaryOperator {

    char applyAsChar(char operand);

    default CharUnaryOperator compose(CharUnaryOperator before) {
        requireNonNull(before);
        return (char v) -> applyAsChar(before.applyAsChar(v));
    }

    default CharUnaryOperator andThen(CharUnaryOperator after) {
        requireNonNull(after);
        return (char t) -> after.applyAsChar(applyAsChar(t));
    }

    static CharUnaryOperator identity() {
        return t -> t;
    }
}
