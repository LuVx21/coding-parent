package org.luvx.coding.common.primitives;

public class Unsigned {
    public static final short unsignedByteMax  = 0xFF;
    public static final int   unsignedShortMax = 0xFFFF;
    public static final long  unsignedIntMax   = 0xFFFFFFFFL;

    public static short unsignedByte(byte b) {
        return (short) (b & 0xFF);
    }

    public static int unsignedShort(short data) {
        return data & 0xFFFF;
    }

    public static long unsignedInt(int data) {
        return data & 0xFFFFFFFFL;
    }
}
