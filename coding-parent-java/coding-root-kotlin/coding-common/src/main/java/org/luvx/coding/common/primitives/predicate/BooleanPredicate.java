package org.luvx.coding.common.primitives.predicate;

import static java.util.Objects.requireNonNull;

public interface BooleanPredicate {

    boolean test(boolean value);

    default BooleanPredicate and(BooleanPredicate other) {
        requireNonNull(other);
        return (value) -> test(value) && other.test(value);
    }

    default BooleanPredicate negate() {
        return (value) -> !test(value);
    }

    default BooleanPredicate or(BooleanPredicate other) {
        requireNonNull(other);
        return (value) -> test(value) || other.test(value);
    }
}
