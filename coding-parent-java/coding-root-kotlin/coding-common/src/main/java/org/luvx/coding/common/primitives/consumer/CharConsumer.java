package org.luvx.coding.common.primitives.consumer;

import static java.util.Objects.requireNonNull;

public interface CharConsumer {
    void accept(char value);

    default CharConsumer andThen(CharConsumer after) {
        requireNonNull(after);
        return (char t) -> { accept(t); after.accept(t); };
    }
}
