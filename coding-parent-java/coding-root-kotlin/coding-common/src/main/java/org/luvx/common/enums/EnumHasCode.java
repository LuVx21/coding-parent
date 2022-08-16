package org.luvx.common.enums;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public interface EnumHasCode<T> {
    /**
     * 枚举类区分值(唯一)
     */
    T getCode();

    default boolean isValidBizCode() {
        return true;
    }

    /**
     * 是否是: 实际用于业务标识的枚举
     */
    static <E extends Enum<E> & EnumHasCode<T>, T> boolean isValidBizCode(final Class<E> enumClass, T code) {
        if (code == null) {
            return false;
        }
        E of = of(enumClass, code);
        return of != null && of.isValidBizCode();
    }

    static <E extends Enum<E> & EnumHasCode<T>, T> boolean isValidCode(final Class<E> enumClass, T code) {
        return of(enumClass, code) != null;
    }

    @Nullable
    static <E extends Enum<E> & EnumHasCode<T>, T> E of(final Class<E> enumClass, T code) {
        if (code == null) {
            return null;
        }
        return enumMap(enumClass).get(code);
    }

    /**
     * 根据code找到对应枚举值, 不存在设置默认值
     */
    static <E extends Enum<E> & EnumHasCode<T>, T> E of(final Class<E> enumClass, T code, @Nonnull E defaultValue) {
        if (code == null) {
            return defaultValue;
        }
        return enumMap(enumClass).getOrDefault(code, defaultValue);
    }

    /**
     * 带有 code 的枚举类的所有枚举值
     * <p>
     * 枚举值很多时可加工使用本地缓存
     *
     * @param enumClass 枚举类
     * @param <E>       枚举类型
     * @param <T>       code 类型
     * @return Map 结构, k:code 值, v:枚举值
     */
    static <E extends Enum<E> & EnumHasCode<T>, T> Map<T, E> enumMap(final Class<E> enumClass) {
        E[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null) {
            return emptyMap();
        }
        return Arrays.stream(enumConstants)
                .collect(toMap(EnumHasCode::getCode, identity(), (a, b) -> b));
    }
}
