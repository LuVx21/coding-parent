package org.luvx.coding.common.idbit.dto;

import org.luvx.coding.common.enums.EnumHasCode;

import java.util.EnumSet;
import java.util.Set;

import static java.util.EnumSet.noneOf;
import static java.util.stream.Collectors.toCollection;

/**
 * bit位作为标志, 使用一个long 型存储多个标志
 * 枚举实现此接口
 * 只用了1位只能表示1/0两种情况, 最多表示64个场景
 * <p>
 * 此类场景有限:
 * 64位不够用或表示的类型多于两个, 可以使用BigInteger,数值采用多进制,如16进制
 */
public interface HasBitInfo extends EnumHasCode<Integer> {
    /**
     * bit位的索引,二进制从右向左,从0开始
     */
    int bitIndex();

    /**
     * 仅指定bit位的整数形式
     */
    default long bitValue() {
        return 1L << bitIndex();
    }

    @Override
    default Integer getCode() {
        return bitIndex();
    }

    /**
     * 整数形式转换为位对应的枚举集合
     */
    static <E extends Enum<E> & HasBitInfo> Set<E> toSet(long value, Class<E> type) {
        return EnumSet.allOf(type).stream()
                .filter(item -> item.bitIndex() >= 0)
                .filter(item -> (value & item.bitValue()) != 0)
                .collect(toCollection(() -> noneOf(type)));
    }

    /**
     * 汇总各bit,返回其整数形式
     */
    static <E extends HasBitInfo> long toLong(Set<E> set) {
        return set.stream()
                .mapToLong(HasBitInfo::bitValue)
                .sum();
    }

    /**
     * 汇总各bit,返回其整数形式(位运算)
     */
    static <E extends HasBitInfo> long toLongBit(Set<E> set) {
        if (set == null || set.isEmpty()) {
            return 0L;
        }
        long bitValue = 0;
        for (HasBitInfo bit : set) {
            bitValue |= bit.bitValue();
        }
        return bitValue;
    }
}