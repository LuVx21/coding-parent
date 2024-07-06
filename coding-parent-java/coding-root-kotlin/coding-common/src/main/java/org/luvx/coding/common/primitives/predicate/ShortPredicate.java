package org.luvx.coding.common.primitives.predicate;

import static java.util.Objects.requireNonNull;

public interface ShortPredicate {

    boolean test(short value);

    default ShortPredicate and(ShortPredicate other) {
        requireNonNull(other);
        return (value) -> test(value) && other.test(value);
    }

    default ShortPredicate negate() {
        return (value) -> !test(value);
    }

    default ShortPredicate or(ShortPredicate other) {
        requireNonNull(other);
        return (value) -> test(value) || other.test(value);
    }
}
