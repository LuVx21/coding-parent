package org.luvx.boot.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class CustomControllerAdvice {
    @ExceptionHandler({Exception.class})
    public Object handException(HttpServletRequest request, Exception e) {
        log.error("异常->url: {}", request.getRequestURI(), e);
        return "";
    }
}
