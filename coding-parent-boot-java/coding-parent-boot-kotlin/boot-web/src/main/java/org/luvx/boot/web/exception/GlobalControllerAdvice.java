package org.luvx.boot.web.exception;

import java.util.List;
import java.util.Objects;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    private static final String BAD_REQUEST_MSG = "客户端请求参数错误";

    /**
     * 处理 form data方式调用接口校验失败抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public Object bindExceptionHandler(BindException e) {
        List<String> msgList = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();
        return List.of(HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_MSG, msgList);
    }

    /**
     * 处理 json 请求体调用接口校验失败抛出的异常s
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<String> collect = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();
        return List.of(HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_MSG, collect);
    }

    /**
     * 处理单个参数校验失败抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Object constraintViolationExceptionHandler(ConstraintViolationException e) {
        List<String> collect = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return List.of(HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_MSG, collect);
    }
}