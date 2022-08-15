package org.luvx.common.enums.ext;

import org.luvx.common.enums.EnumHasName;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface EnumNameAnno {
    Class<? extends EnumHasName<?, ?>> value();

    String methodName() default "getName";
}