package org.luvx.common.idbit.dto;

import java.util.EnumSet;
import java.util.Set;

import static java.util.EnumSet.noneOf;
import static java.util.stream.Collectors.toCollection;

/**
 * bit位作为标志, 使用一个long 型存储多个标志
 * 枚举实现此接口
 */
public interface HasBitInfo {
    int bitIndex();

    default long bitValue() {
        return 1L << bitIndex();
    }

    static <E extends Enum<E> & HasBitInfo> Set<E> toSet(long value, Class<E> type) {
        return EnumSet.allOf(type).stream()
                .filter(item -> (value & item.bitValue()) != 0)
                .collect(toCollection(() -> noneOf(type)));
    }

    static <E extends HasBitInfo> long toLong(Set<E> set) {
        return set.stream()
                .mapToLong(HasBitInfo::bitValue)
                .sum();
    }
}