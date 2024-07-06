package org.luvx.coding.common.primitives.predicate;

import static java.util.Objects.requireNonNull;

public interface BytePredicate {

    boolean test(byte value);

    default BytePredicate and(BytePredicate other) {
        requireNonNull(other);
        return (value) -> test(value) && other.test(value);
    }

    default BytePredicate negate() {
        return (value) -> !test(value);
    }

    default BytePredicate or(BytePredicate other) {
        requireNonNull(other);
        return (value) -> test(value) || other.test(value);
    }
}
