package org.luvx.coding.common.util;

import java.util.Objects;

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

    /**
     * 指定区间内替换
     * <pre>
     *     abcdefg -> 2~5 替换为 12345 -> ab12345g
     * </pre>
     */
    public static String replace(String s, int from, int to, String replacement) {
        int sLen = org.apache.commons.lang3.StringUtils.length(s);
        int replaceLen = org.apache.commons.lang3.StringUtils.length(replacement);
        int length = Objects.checkIndex(to, sLen) - Objects.checkIndex(from, sLen) + 1;
        if (length < 1) {
            throw new RuntimeException(STR."from(\{from})~to(\{to})异常");
        }
        char[] charArray = new char[sLen - length + replaceLen];
        int k = 0;
        for (int i = 0; i < from; i++) {
            charArray[k++] = s.charAt(i);
        }
        for (int i = 0; i < replaceLen; i++) {
            charArray[k++] = replacement.charAt(i);
        }
        for (int i = to + 1; i < sLen; i++) {
            charArray[k++] = s.charAt(i);
        }
        return String.valueOf(charArray);
    }
}
