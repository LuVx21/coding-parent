package org.luvx.common.enums;

public interface EnumHasName<CODE, NAME> extends EnumHasCode<CODE> {
    NAME getName();
}
