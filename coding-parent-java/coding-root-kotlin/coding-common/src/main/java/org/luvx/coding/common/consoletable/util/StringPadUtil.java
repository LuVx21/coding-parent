package org.luvx.coding.common.consoletable.util;

import com.google.common.base.Charsets;

public class StringPadUtil {
    private static final String padChar = " ";

    public static String leftPad(String str, int size) {
        if (str == null) {
            return "";
        }
        int strLength = strLength(str);
        if (size <= 0 || size <= strLength) {
            return str;
        }
        return padChar.repeat(size - strLength).concat(str);
    }

    public static String rightPad(String str, int size) {
        if (str == null) {
            return "";
        }
        int strLength = strLength(str);
        if (size <= 0 || size <= strLength) {
            return str;
        }
        return str.concat(padChar.repeat(size - strLength));
    }

    public static String center(String str, int size) {
        if (str == null) {
            return "";
        }
        int strLength = strLength(str);
        if (size <= 0 || size <= strLength) {
            return str;
        }
        str = leftPad(str, strLength + (size - strLength) / 2);
        str = rightPad(str, size);
        return str;
    }

    public static int strLength(String str) {
        int len = 0, j = 0;
        byte[] bytes = str.getBytes(Charsets.UTF_8);
        while (bytes.length > 0) {
            short tmpst = (short) (bytes[j] & 0xF0);
            if (tmpst >= 0xB0) {
                if (tmpst < 0xC0) {
                    j += 2;
                    len += 2;
                } else if ((tmpst == 0xC0) || (tmpst == 0xD0)) {
                    j += 2;
                    len += 2;
                } else if (tmpst == 0xE0) {
                    j += 3;
                    len += 2;
                } else if (tmpst == 0xF0) {
                    short tmpst0 = (short) (((short) bytes[j]) & 0x0F);
                    if (tmpst0 == 0) {
                        j += 4;
                        len += 2;
                    } else if ((tmpst0 > 0) && (tmpst0 < 12)) {
                        j += 5;
                        len += 2;
                    } else if (tmpst0 > 11) {
                        j += 6;
                        len += 2;
                    }
                }
            } else {
                j += 1;
                len += 1;
            }
            if (j > bytes.length - 1) {
                break;
            }
        }
        return len;
    }
}
