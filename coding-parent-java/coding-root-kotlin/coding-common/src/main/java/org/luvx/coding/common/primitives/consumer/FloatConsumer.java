package org.luvx.coding.common.primitives.consumer;

import static java.util.Objects.requireNonNull;

public interface FloatConsumer {
    void accept(float value);

    default FloatConsumer andThen(FloatConsumer after) {
        requireNonNull(after);
        return (float t) -> { accept(t); after.accept(t); };
    }
}
