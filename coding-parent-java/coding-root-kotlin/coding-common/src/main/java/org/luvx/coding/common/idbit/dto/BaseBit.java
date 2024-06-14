package org.luvx.coding.common.idbit.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.Set;

/**
 * 一个具体的long型数据及其所对应的bit位flag
 */
@Getter
@ToString
public abstract class BaseBit<T extends Enum<T> & HasBitInfo> {
    private final long   data;
    private final Set<T> bits;

    /**
     * 这里实际只需要一个data, 因不方便获取泛型的实际类型, 才有了bits参数
     */
    protected BaseBit(long data, Set<T> bits) {
        this.data = data;
        this.bits = bits;
    }

    /**
     * value 数据对应的bit位是否为1
     */
    public boolean positive(T value) {
        return bits.contains(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseBit<?> baseBit = (BaseBit<?>) o;
        if (data != baseBit.data) {
            return false;
        }
        return Objects.equals(bits, baseBit.bits);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(data);
        result = 31 * result + (bits != null ? bits.hashCode() : 0);
        return result;
    }
}
