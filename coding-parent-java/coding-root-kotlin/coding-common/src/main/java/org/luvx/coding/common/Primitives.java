package org.luvx.coding.common;

import java.lang.reflect.Field;

public class Primitives {
    public static boolean isPrimitive(Object o) {
        return isPrimitive(o.getClass());
    }

    public static boolean isPrimitive(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }

        try {
            Field type = clazz.getField("TYPE");
            return ((Class<?>) type.get(null)).isPrimitive();
        } catch (Exception _) {
        }
        return false;
    }
}
