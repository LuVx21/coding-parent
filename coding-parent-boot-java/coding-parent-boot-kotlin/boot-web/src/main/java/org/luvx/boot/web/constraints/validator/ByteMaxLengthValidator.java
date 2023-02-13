package org.luvx.boot.web.constraints.validator;

import org.luvx.boot.web.constraints.ByteMaxLength;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.nio.charset.Charset;

public class ByteMaxLengthValidator implements ConstraintValidator<ByteMaxLength, String> {
    private int     max;
    private Charset charset;

    @Override
    public void initialize(ByteMaxLength constraintAnnotation) {
        max = constraintAnnotation.max();
        charset = Charset.forName(constraintAnnotation.charset());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        boolean result = value.getBytes(charset).length > max;
        if (!result) {
            return false;
        }

        int chBytes = "中".getBytes(charset).length;
        int chMax = max / chBytes;

        //拿到枚举中的message，并替换变量，这个变量是我自己约定的，
        //约定了两个绑定变量：chMax 和 enMax
        String message = constraintValidatorContext
                .getDefaultConstraintMessageTemplate()
                .replace("{chMax}", String.valueOf(chMax))
                .replace("{enMax}", String.valueOf(max));

        //禁用默认值，否则会有两条message
        constraintValidatorContext.disableDefaultConstraintViolation();

        //添加新的message
        constraintValidatorContext.
                buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return true;
    }
}