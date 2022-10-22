package org.luvx.boot.web.constraints;

import org.luvx.boot.web.constraints.validator.EnumRangeValidator;
import org.luvx.coding.common.enums.EnumHasCode;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {EnumRangeValidator.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(EnumRange.List.class)
public @interface EnumRange {
    String message() default "参数值不正确,值域:{}";

    Class<? extends EnumHasCode<?>> enumType();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
            ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        EnumRange[] value();
    }
}
