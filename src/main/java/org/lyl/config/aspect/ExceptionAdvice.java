package org.lyl.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.lyl.entity.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "org.lyl")
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<Void> arithmeticExceptionAdvice(Exception e) {
        String message = e.getMessage();
        return Response.error(message);
    }



}
