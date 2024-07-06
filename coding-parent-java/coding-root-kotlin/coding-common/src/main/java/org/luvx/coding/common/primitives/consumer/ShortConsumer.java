package org.luvx.coding.common.primitives.consumer;

import static java.util.Objects.requireNonNull;

public interface ShortConsumer {
    void accept(short value);

    default ShortConsumer andThen(ShortConsumer after) {
        requireNonNull(after);
        return (short t) -> { accept(t); after.accept(t); };
    }
}
