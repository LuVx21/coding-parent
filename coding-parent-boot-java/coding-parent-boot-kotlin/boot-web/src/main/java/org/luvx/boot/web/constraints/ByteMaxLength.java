package org.luvx.boot.web.constraints;

import org.luvx.boot.web.constraints.validator.ByteMaxLengthValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {ByteMaxLengthValidator.class})
@Target({
        ElementType.METHOD, ElementType.FIELD,
        ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER, ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ByteMaxLength.List.class)
public @interface ByteMaxLength {
    /**
     * 最大字节长度
     */
    int max() default Integer.MAX_VALUE;

    String charset() default "UTF-8";

    Class<?>[] groups() default {};

    String message() default "【${validatedValue}】的字节数已经超过最大值{max}";

    Class<? extends Payload>[] payload() default {};

    @Target({
            ElementType.METHOD, ElementType.FIELD,
            ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
            ElementType.PARAMETER, ElementType.TYPE_USE
    })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ByteMaxLength[] value();
    }
}