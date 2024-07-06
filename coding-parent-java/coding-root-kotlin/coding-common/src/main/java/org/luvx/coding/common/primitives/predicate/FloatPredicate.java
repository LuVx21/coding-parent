package org.luvx.coding.common.primitives.predicate;

import static java.util.Objects.requireNonNull;

public interface FloatPredicate {

    boolean test(float value);

    default FloatPredicate and(FloatPredicate other) {
        requireNonNull(other);
        return (value) -> test(value) && other.test(value);
    }

    default FloatPredicate negate() {
        return (value) -> !test(value);
    }

    default FloatPredicate or(FloatPredicate other) {
        requireNonNull(other);
        return (value) -> test(value) || other.test(value);
    }
}
