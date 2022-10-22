package org.luvx.coding.common.enums;

public interface EnumHasUnknown<E extends Enum<E> & EnumHasUnknown<E>> {
    E unknown();
}
