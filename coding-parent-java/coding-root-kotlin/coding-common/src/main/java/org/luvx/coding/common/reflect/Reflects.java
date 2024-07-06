package org.luvx.coding.common.reflect;

import org.apache.commons.lang3.ObjectUtils;

import jakarta.annotation.Nullable;
import java.net.URL;

public class Reflects {
    @Nullable
    public static Class<?> forPrimitiveBoxName(String primitiveName) {
        return switch (primitiveName) {
            // Integral types
            case "int" -> Integer.TYPE;
            case "long" -> Long.TYPE;
            case "short" -> Short.TYPE;
            case "char" -> Character.TYPE;
            case "byte" -> Byte.TYPE;

            // Floating-point types
            case "float" -> Float.TYPE;
            case "double" -> Double.TYPE;

            // Other types
            case "boolean" -> Boolean.TYPE;
            case "void" -> Void.TYPE;

            default -> null;
        };
    }

    public static URL projectPath(String file) {
        return Reflects.class.getClassLoader().getResource(ObjectUtils.defaultIfNull(file, ""));
    }
}
