package org.luvx.coding.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TimeUtils {
    public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final DateFormat NORM_DATETIME_FORMAT = new SimpleDateFormat(NORM_DATETIME_PATTERN);

    private TimeUtils() {
        throw new IllegalStateException();
    }
}
