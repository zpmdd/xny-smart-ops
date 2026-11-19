package com.xny.framework.exception;


import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.collection.CollUtil;
import com.xny.common.utils.R;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotLoginException.class)
    public R<String> NotLoginExceptionHandler(NotLoginException ex){
        ex.printStackTrace();
        String message = ex.getMessage();
        if(message.contains("Token无效")){
            return R.failed(2,"请先登录");
        }else if(message.contains("Token已被踢下线")){
            return R.failed(2,"您已被踢下线");
        }else if(message.contains("Token已过期")){
            return R.failed(2,"登录超时");
        }else if(message.contains("Token已被顶下线")){
            return R.failed(2,"您已被顶下线");
        }
        return R.failed(2,ex.getMessage());
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R<String> ConstraintViolationExceptionHandler(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        return R.failed(iterator.next().getMessageTemplate());
    }
    /**
    * MethodArgumentNotValidException
    */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<String> MethodArgumentNotValidHandler(
            MethodArgumentNotValidException exception) {
        return R.failed(exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R<String> handlerException(Exception e) {
        if (e instanceof NullPointerException) {
            e.printStackTrace();
            return R.failed("空指针异常");

        }
        if (e instanceof NoHandlerFoundException) {
            e.printStackTrace();
            return R.failed("请求不存在");

        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            e.printStackTrace();
            return R.failed("请求方式不支持");

        } else if (e instanceof MethodArgumentNotValidException) {
            e.printStackTrace();
            return R.failed("参数异常错误");

        } else if (e instanceof ConstraintViolationException) {
            e.printStackTrace();
            return R.failed(CollUtil.getFirst(((ConstraintViolationException) e).getConstraintViolations()).getMessage());

        } else if (e instanceof MethodArgumentTypeMismatchException) {
            e.printStackTrace();
            return R.failed("服务器内部异常");

        } else if (e instanceof HttpMessageNotReadableException) {
            e.printStackTrace();
            return R.failed(((HttpMessageNotReadableException) e).getMessage());

        } else if (e instanceof IllegalArgumentException) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        e.printStackTrace();
        return R.failed(e.getMessage());
    }
}
