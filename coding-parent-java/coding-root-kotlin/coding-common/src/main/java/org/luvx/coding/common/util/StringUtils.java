package org.luvx.coding.common.util;

public class StringUtils {
    public static int estimatedBinaryLengthAsUTF8(String value) {
        int length = 0;
        for (int i = 0; i < value.length(); i++) {
            char var10 = value.charAt(i);
            if (var10 < 0x80) {
                length += 1;
            } else if (var10 < 0x800) {
                length += 2;
            } else if (Character.isSurrogate(var10)) {
                length += 4;
                i++;
            } else {
                length += 3;
            }
        }
        return length;
    }

    public static String removeChar(String s, char c) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                return removeChar(s, c, i);
            }
        }
        return s;
    }

    public static String removeChar(String s, char c, int startIndex) {
        StringBuilder sb = new StringBuilder(s.length() - 1);
        sb.append(s, 0, startIndex);
        for (int i = startIndex + 1; i < s.length(); i++) {
            char charOfString = s.charAt(i);
            if (charOfString != c) {
                sb.append(charOfString);
            }
        }
        return sb.toString();
    }
}
