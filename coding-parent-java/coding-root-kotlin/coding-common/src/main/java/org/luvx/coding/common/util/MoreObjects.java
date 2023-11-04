package org.luvx.coding.common.util;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;

public class MoreObjects {
    public static List<Object> duplicate(Object... array) {
        return appearCount(1, array);
    }

    public static List<Object> appearCount(int count, Object... array) {
        if (ArrayUtils.isEmpty(array)) {
            return emptyList();
        }
        return Arrays.stream(array)
                .collect(groupingBy(e -> e, counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > count)
                .map(Map.Entry::getKey)
                .collect(toList());
    }
}
