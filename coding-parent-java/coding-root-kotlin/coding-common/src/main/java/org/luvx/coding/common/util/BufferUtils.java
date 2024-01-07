package org.luvx.coding.common.util;

import java.nio.ByteBuffer;

public class BufferUtils {
    public static byte[] getBytes(final ByteBuffer buffer) {
        final byte[] b = new byte[buffer.remaining()];
        buffer.get(b);
        return b;
    }
}
