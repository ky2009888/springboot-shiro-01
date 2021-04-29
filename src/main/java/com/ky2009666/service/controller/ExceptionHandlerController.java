package com.ky2009666.service.controller;

import com.ky2009666.service.domain.AjaxResult;
import com.ky2009666.service.exception.CustomRuntimeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ky2009666
 * @description Exception处理
 * @date 2021/4/28
 **/
@RestControllerAdvice
public class ExceptionHandlerController {
    //该注解表示这个方法可以处理所有的异常
    @ExceptionHandler(Exception.class)
    public Object handlerException(HttpServletRequest request, Exception e) {
        //自定义结果集Result
        AjaxResult result = new AjaxResult();
        result.put("result_code", "500");
        //判断是否为自定义异常
        if (e instanceof CustomRuntimeException) {
            result.put("message", e.getMessage());
        } else {
            e.printStackTrace();
            result.put("message", "未知异常！请联系管理员！");
        }
        //检查请求是否为ajax, 如果是 ajax 请求则返回 Result json串, 如果不是 ajax 请求则返回 error 视图
        String contentTypeHeader = request.getHeader("Content-Type");
        String acceptHeader = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
        if ((contentTypeHeader != null && contentTypeHeader.contains("application/json"))
                || (acceptHeader != null && acceptHeader.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith)) {
            return result;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("message", e.getMessage());
            modelAndView.addObject("url", request.getRequestURL());
            modelAndView.addObject("stackTrace", e.getStackTrace());
            modelAndView.setViewName("error/error");
            return modelAndView;
        }
    }
}
