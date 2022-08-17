package org.luvx.boot.web.exception;

import org.luvx.boot.common.exception.BizException;
import org.luvx.boot.web.response.BizEnum;
import org.luvx.boot.web.response.R;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalControllerAdvice {
    /**
     * 处理 form data方式调用接口校验失败抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public R<List<String>> bindExceptionHandler(BindException e) {
        List<String> msgList = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();
        return R.fail(BizEnum.BAD_REQUEST_MSG, msgList);
    }

    /**
     * 处理 json 请求体调用接口校验失败抛出的异常s
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<List<String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<String> msgList = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();
        return R.fail(BizEnum.BAD_REQUEST_MSG, msgList);
    }

    /**
     * 处理单个参数校验失败抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<List<String>> constraintViolationExceptionHandler(ConstraintViolationException e) {
        List<String> msgList = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return R.fail(BizEnum.BAD_REQUEST_MSG, msgList);
    }

    @ExceptionHandler(BizException.class)
    public R<String> customHandler(BizException e) {
        return R.fail(e.getMsg(), null);
    }

    @ExceptionHandler(Exception.class)
    public R<String> exceptionHandler(Throwable t) {
        return R.fail();
    }
}