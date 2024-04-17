package org.luvx.boot.web.exception;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.luvx.boot.web.filter.WebLogFilter;
import org.luvx.boot.web.response.R;
import org.luvx.coding.common.exception.BizException;
import org.luvx.coding.common.exception.base.BaseException;
import org.luvx.coding.common.exception.base.ResponseCode;
import org.luvx.coding.common.exception.code.CommonResponseCode;
import org.luvx.coding.common.exception.code.ServletResponseCode;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.luvx.coding.common.exception.code.ArgumentResponseCode.BAD_REQUEST_MSG;

@Slf4j
@Component
@ControllerAdvice
@ConditionalOnWebApplication
// @ConditionalOnMissingBean(UnifiedExceptionHandler.class)
public class UnifiedExceptionHandler {

    @Value("${app.switch.exception.handler.enable:false}")
    private boolean exceptionHandlerEnable;

    /**
     * 自定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ResponseBody
    @ExceptionHandler(BaseException.class)
    public R<?> handleBaseException(BaseException e) {
        log.error(e.getMessage(), e);
        return R.fail(e.getResponseCode(), getMessage(e));
    }

    /**
     * 业务异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ResponseBody
    @ExceptionHandler(BizException.class)
    public R<?> handleBusinessException(BaseException e) {
        log.error(e.getMessage(), e);
        return R.fail(e.getResponseCode(), getMessage(e));
    }

    /**
     * Controller上一层相关异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ResponseBody
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            // BindException.class,
            // MethodArgumentNotValidException.class
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public R<?> handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        ResponseCode code = CommonResponseCode.SERVER_ERROR;
        try {
            code = ServletResponseCode.valueOf(e.getClass().getSimpleName());
        } catch (IllegalArgumentException e1) {
            log.error("class [{}] not defined in enum {}", e.getClass().getName(),
                    ServletResponseCode.class.getName());
        }

        if (exceptionHandlerEnable) {
            code = CommonResponseCode.SERVER_ERROR;
            BaseException ee = CommonResponseCode.SERVER_ERROR.exception();
            String message = getMessage(ee);
            return R.fail(code, message);
        }

        return R.fail(code, e.getMessage());
    }

    /**
     * 参数绑定异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public R<?> handleBindException(BindException e) {
        log.error("参数绑定校验异常", e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 处理单个参数校验失败抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<List<String>> constraintViolationExceptionHandler(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("|"));
        return R.fail(BAD_REQUEST_MSG, msg);
    }

    /**
     * 未定义异常
     *
     * @param t 异常
     * @return 异常结果
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R<?> handleException(HttpServletRequest request, Throwable t) {
        String info = WebLogFilter.getRequestParams(request);
        log.error("异常->url: {}", info, t);
        if (exceptionHandlerEnable) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
            BaseException ee = CommonResponseCode.SERVER_ERROR.exception();
            String message = getMessage(ee);
            return R.fail(CommonResponseCode.SERVER_ERROR, message);
        }

        return R.fail(CommonResponseCode.SERVER_ERROR, t.getMessage());
    }

    /**
     * 包装绑定异常结果
     *
     * @param result 绑定结果
     * @return 异常结果
     */
    private R<?> wrapperBindingResult(BindingResult result) {
        String msg = result.getAllErrors().stream()
                .map(error -> {
                    String s = "";
                    if (error instanceof FieldError) {
                        String field = ((FieldError) error).getField();
                        s = STR."\{field}:";
                    }
                    s += defaultIfNull(error.getDefaultMessage(), "");
                    return s;
                })
                .collect(Collectors.joining("|"));
        return R.fail(BAD_REQUEST_MSG, msg);
    }

    /**
     * 获取国际化消息
     *
     * @param e 异常
     */
    private String getMessage(BaseException e) {
        String message = e.getResponseCode().getMessage();
        return StringUtils.isEmpty(message) ? e.getMessage() : STR."response.\{message}";
    }
}