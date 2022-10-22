package org.luvx.boot.web.constraints.validator;

import org.luvx.boot.web.constraints.EnumRange;
import org.luvx.coding.common.enums.EnumHasCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class EnumRangeValidator implements ConstraintValidator<EnumRange, Object> {

    private Class<? extends EnumHasCode<?>> clazz;

    @Override
    public void initialize(EnumRange anno) {
        clazz = anno.enumType();
        if (clazz == null) {
            throw new IllegalArgumentException("@EnumRange.enumType 不能为空");
        }
        if (!Enum.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz.getName() + "不是枚举类");
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        EnumHasCode<?>[] enums = clazz.getEnumConstants();
        boolean present = Arrays.stream(enums)
                .filter(EnumHasCode::isValidBizCode)
                .anyMatch(e -> Objects.equals(e.getCode(), value));
        if (!present) {
            String enumCodeStr = Arrays.stream(enums)
                    .filter(EnumHasCode::isValidBizCode)
                    .map(EnumHasCode::getCode)
                    .map(Object::toString)
                    .collect(Collectors.joining("/"));
            String message = context
                    .getDefaultConstraintMessageTemplate()
                    .replace("{}", enumCodeStr);

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }
        return present;
    }
}
