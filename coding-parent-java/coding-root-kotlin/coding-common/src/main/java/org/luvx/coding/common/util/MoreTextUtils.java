package org.luvx.coding.common.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class MoreTextUtils {
    public static final String SPLITOR = ",";

    /**
     * 向逗号分割的字符串中添加元素
     */
    public static String addItem(String str, Collection<String> addList) {
        str = ObjectUtils.defaultIfNull(str, "");
        if (CollectionUtils.isEmpty(addList)) {
            return str;
        }

        return Stream.concat(Arrays.stream(str.split(SPLITOR)), addList.stream())
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.joining(SPLITOR));
    }

    /**
     * 从逗号分割的字符串中删除元素
     */
    public static String removeItem(String str, Collection<String> removeList) {
        if (StringUtils.isEmpty(str) || CollectionUtils.isEmpty(removeList)) {
            return ObjectUtils.defaultIfNull(str, "");
        }

        return Arrays.stream(str.split(SPLITOR))
                .filter(s -> !removeList.contains(s))
                .distinct()
                .collect(Collectors.joining(SPLITOR));
    }

    public static List<String> parse2List(String str) {
        if (StringUtils.isEmpty(str)) {
            return Collections.emptyList();
        }

        return Arrays.stream(str.split(SPLITOR))
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
    }
}
