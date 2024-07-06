package org.luvx.coding.common.primitives.predicate;

import static java.util.Objects.requireNonNull;

public interface CharPredicate {

    boolean test(char value);

    default CharPredicate and(CharPredicate other) {
        requireNonNull(other);
        return (value) -> test(value) && other.test(value);
    }

    default CharPredicate negate() {
        return (value) -> !test(value);
    }

    default CharPredicate or(CharPredicate other) {
        requireNonNull(other);
        return (value) -> test(value) || other.test(value);
    }
}
