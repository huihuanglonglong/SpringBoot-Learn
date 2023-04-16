package org.lyl.entity;

import lombok.Data;
import org.lyl.common.util.CommonConst;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {

    private Integer code;

    private String msg;

    private T data;

    public Response() {

    }

    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static Response success() {
       return new Response<>(CommonConst.SUCCESS_CODE, CommonConst.SUCCESS_MSG);
    }

    public static <T> Response<T> success(T data) {
        Response<T> rep = new Response<>();
        rep.setData(data);
        return rep;
    }

    public static Response error() {
        return new Response(CommonConst.COMMON_ERROR_CODE, CommonConst.COMMON_ERROR_MSG);
    }


    public static Response error(Integer errorCode) {
        return new Response(errorCode, CommonConst.COMMON_ERROR_MSG);
    }

    public static Response error(String errorMsg) {
        return new Response(CommonConst.COMMON_ERROR_CODE, errorMsg);
    }

    public static Response error(Integer errorCode, String errorMsg) {
        return new Response(errorCode, errorMsg);
    }




}
