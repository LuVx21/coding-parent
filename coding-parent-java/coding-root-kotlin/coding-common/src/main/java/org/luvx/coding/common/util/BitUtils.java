package org.luvx.coding.common.util;

/**
 * 位操作辅助类
 */
public class BitUtils {

    private BitUtils() {
        throw new UnsupportedOperationException();
    }

    private static long set(long bitmap, int maxBit, int bit, boolean setOrUnset) {
        if (bit >= maxBit) {
            throw new RuntimeException("bitmap set index " + bit + " out of range " + maxBit);
        }
        if (setOrUnset) {
            bitmap |= (1L << bit);
        } else {
            bitmap &= ~(1L << bit);
        }
        return bitmap;
    }

    /**
     * 将bitmap中的某一位设置或取消标志
     *
     * @return 返回修改后的bitmap
     */
    public static long set(long bitmap, int bit, boolean setOrUnset) {
        return set(bitmap, Long.SIZE, bit, setOrUnset);
    }

    /**
     * 将bitmap中的某一位设置为1
     *
     * @return 返回修改后的bitmap
     */
    public static long set(long bitmap, int bit) {
        return set(bitmap, bit, true);
    }

    /**
     * 将bitmap中的某一位设置为0
     *
     * @return 返回修改后的bitmap
     */
    public static long unset(long bitmap, int bit) {
        return set(bitmap, bit, false);
    }

    /**
     * 将bitmap中的某一位设置或取消标志
     *
     * @return 返回修改后的bitmap
     */
    public static int set(int bitmap, int bit, boolean setOrUnset) {
        return (int) set(bitmap, Integer.SIZE, bit, setOrUnset);
    }

    /**
     * 将bitmap中的某一位设置为1
     *
     * @return 返回修改后的bitmap
     */
    public static int set(int bitmap, int bit) {
        return set(bitmap, bit, true);
    }

    /**
     * 将bitmap中的某一位设置为0
     *
     * @return 返回修改后的bitmap
     */
    public static int unset(int bitmap, int bit) {
        return set(bitmap, bit, false);
    }

    /**
     * 将bitmap中的某一位设置或取消标志
     *
     * @return 返回修改后的bitmap
     */
    public static short set(short bitmap, int bit, boolean setOrUnset) {
        return (short) set(bitmap, Short.SIZE, bit, setOrUnset);
    }

    /**
     * 将bitmap中的某一位设置为1
     *
     * @return 返回修改后的bitmap
     */
    public static short set(short bitmap, int bit) {
        return set(bitmap, bit, true);
    }

    /**
     * 将bitmap中的某一位设置为0
     *
     * @return 返回修改后的bitmap
     */
    public static short unset(short bitmap, int bit) {
        return set(bitmap, bit, false);
    }

    /**
     * 将bitmap中的某一位设置或取消标志
     *
     * @return 返回修改后的bitmap
     */
    public static byte set(byte bitmap, int bit, boolean setOrUnset) {
        return (byte) set((long) bitmap, Byte.SIZE, bit, setOrUnset);
    }

    /**
     * 将bitmap中的某一位设置为1
     *
     * @return 返回修改后的bitmap
     */
    public static byte set(byte bitmap, int bit) {
        return set(bitmap, bit, true);
    }

    /**
     * 将bitmap中的某一位设置为0
     *
     * @return 返回修改后的bitmap
     */
    public static byte unset(byte bitmap, int bit) {
        return set(bitmap, bit, false);
    }

    protected static boolean test(long bitmap, int maxBit, int bit) {
        if (bit >= maxBit) {
            throw new RuntimeException("bitmap set index " + bit + " out of range " + maxBit);
        }
        return (bitmap & (1L << bit)) != 0;
    }

    /**
     * 判断bitmap中某一位是否被设置
     *
     * @return 如果被设置，返回true，否则返回false
     */
    public static boolean test(long bitmap, int bit) {
        return test(bitmap, Long.SIZE, bit);
    }

    /**
     * 判断bitmap中某一位是否被设置
     *
     * @return 如果被设置，返回true，否则返回false
     */
    public static boolean test(int bitmap, int bit) {
        return test(bitmap, Integer.SIZE, bit);
    }

    /**
     * 判断bitmap中某一位是否被设置
     *
     * @return 如果被设置，返回true，否则返回false
     */
    public static boolean test(short bitmap, int bit) {
        return test(bitmap, Short.SIZE, bit);
    }

    /**
     * 判断bitmap中某一位是否被设置
     *
     * @return 如果被设置，返回true，否则返回false
     */
    public static boolean test(byte bitmap, int bit) {
        return test(bitmap, Byte.SIZE, bit);
    }
}
