package org.lyl.config.advice;

import org.lyl.entity.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<Void> arithmeticExceptionAdvice(Exception e) {
        String message = e.getMessage();
        return Response.error(message);
    }



}
