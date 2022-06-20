package org.luvx.common.enums;

public interface EnumHasUnknown<E extends Enum<E> & EnumHasUnknown<E>> {
    E unknown();
}
