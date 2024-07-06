package org.luvx.coding.common.primitives.consumer;

import static java.util.Objects.requireNonNull;

public interface BooleanConsumer {
    void accept(boolean value);

    default BooleanConsumer andThen(BooleanConsumer after) {
        requireNonNull(after);
        return (boolean t) -> { accept(t); after.accept(t); };
    }
}
