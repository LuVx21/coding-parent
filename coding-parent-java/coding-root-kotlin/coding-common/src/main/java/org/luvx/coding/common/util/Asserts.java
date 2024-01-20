package org.luvx.coding.common.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class Asserts {
    public static <T> T checkNotEmpty(T reference, Object errorMessage) {
        if (ObjectUtils.isEmpty(reference)) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static String checkNotBlank(String s, Object errorMessage) {
        if (StringUtils.isBlank(s)) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
        return s;
    }
}
