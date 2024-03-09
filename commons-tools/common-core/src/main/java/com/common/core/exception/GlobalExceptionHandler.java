package com.common.core.exception;

import com.common.core.constant.HttpStatus;
import com.common.core.domain.Result;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 *
 * @Author: TuoYingtao
 * @Date: 2023-08-31 16:01:17
 * @Version: v1.0.0
*/
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return Result.fail(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    /**
     * Token异常
     */
    @ExceptionHandler(JwtException.class)
    public Result handleJwtException(JwtException e,HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',认证失败'{}'", requestURI, e.getMessage());
        return Result.fail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return Result.fail(HttpStatus.BAD_METHOD,"不支持" + e.getMessage() + "请求");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleServiceException(BusinessException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',业务异常'{}'",requestURI, e.getMessage(), e);
        Integer code = e.getCode();
        return code == null ? Result.fail(e.getMessage()) : Result.fail((int) code, e.getMessage());
    }

    /**
     * 断言-业务异常
     */
    @ExceptionHandler(BusinessAssertException.class)
    public Result handlerBusinessException(BusinessAssertException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Throwable cause = e.getCause() == null ? e : e.getCause();
        LOGGER.error("请求地址'{}',业务异常'{}'\r\n", requestURI, e.getMessage(), cause);
        return Result.fail(e.getResponseEnum().getCode(), e.getMessage());
    }

    /**
     * 缺少所需的请求体异常处理
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handlerHttpMessageNotReadableException(HttpMessageNotReadableException e,HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',缺少所需的请求体",requestURI,e.getMessage(),e);
        return Result.fail(HttpStatus.BAD_REQUEST,"缺少所需的请求体");
    }

    /**
     * 校验异常：前段以json格式有效
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e,HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',校验异常'{}'",requestURI, e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.fail(HttpStatus.BAD_REQUEST,message);
    }

    /**
     * 校验异常：表单提交有效，对于以json格式提交将会失效
     */
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e,HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',校验异常'{}'",requestURI,e.getMessage(), e);
        String message = "";
        BindingResult bindingResult = e.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message += fieldError.getField() + "：" + fieldError.getDefaultMessage() + "!";
        }
        return Result.fail(HttpStatus.BAD_REQUEST,message);
    }

    /**
     * 缺少所需的请求参数异常处理
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handlerMissingParameter(MissingServletRequestParameterException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',缺少所需的请求参数'{}'", requestURI, e.getMessage(), e);
        String parameterName = e.getParameterName();
        String message = new StringBuilder().append("缺少所需的请求参数:").append(parameterName).toString();
        return Result.fail(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * 校验异常：参数上加@RequestParam或参数加@NotBlank @NotNull等
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result ConstraintViolationExceptionHandler(ConstraintViolationException e,HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        List<String> msgList = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            msgList.add(cvl.getMessageTemplate());
        }
        LOGGER.error("请求地址'{}',校验异常'{}'", requestURI, e.getMessage(), e);
        return Result.fail(HttpStatus.BAD_REQUEST,String.join(",",msgList));
    }

    /**
     * SQL异常
     */
    @ExceptionHandler(SQLException.class)
    public Result handleSQLException(SQLException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}'，发生SQL异常",requestURI,e);
        return Result.fail(HttpStatus.ERROR,e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',发生未知异常.", requestURI, e);
        return Result.fail(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',发生系统异常.", requestURI, e);
        return Result.fail(e.getMessage());
    }

    /**
     * 响应异常
     */
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity handleResponseException(ResponseException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOGGER.error("请求地址'{}',发生系统异常.", requestURI, e);
        return new ResponseEntity<>(e.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
