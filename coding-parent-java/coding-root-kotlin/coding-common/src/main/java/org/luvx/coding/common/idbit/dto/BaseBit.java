package org.luvx.coding.common.idbit.dto;

import lombok.Getter;

import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

@Getter
public abstract class BaseBit<T extends HasBitInfo> {
    private final long   id;
    private final Set<T> bits;

    protected BaseBit(long id, Set<T> bits) {
        this.id = id;
        this.bits = bits;
    }

    @Override
    public String toString() {
        return reflectionToString(this, SHORT_PREFIX_STYLE);
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
        if (id != baseBit.id) {
            return false;
        }
        return Objects.equals(bits, baseBit.bits);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (bits != null ? bits.hashCode() : 0);
        return result;
    }
}
