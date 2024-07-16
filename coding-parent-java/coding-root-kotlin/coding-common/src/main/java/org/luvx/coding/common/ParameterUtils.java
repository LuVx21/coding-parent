package org.luvx.coding.common;

import org.apache.commons.lang3.StringUtils;

public class ParameterUtils {
    public static Object parseParameter(String value, String classType) {
        if (StringUtils.isBlank(classType)) {
            return value;
        }

        return switch (classType.toLowerCase()) {
            case "byte", "java.lang.byte" -> Byte.parseByte(value);
            case "short", "java.lang.short" -> Short.parseShort(value);
            case "int", "integer", "java.lang.integer" -> Integer.parseInt(value);
            case "long", "java.lang.long" -> Long.parseLong(value);
            case "boolean", "java.lang.boolean" -> Boolean.parseBoolean(value);
            case "float", "java.lang.float" -> Float.parseFloat(value);
            case "double", "java.lang.double" -> Double.parseDouble(value);
            case "char", "character", "java.lang.character" -> {
                char[] array = value.toCharArray();
                yield array.length > 0 ? array[0] : null;
            }
            default -> value;
        };
    }
}
