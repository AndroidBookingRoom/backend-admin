package com.example.backend.domain;

import com.example.backend.common.Constants;
import com.example.backend.utils.enums.ErrorCodes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by NhanNguyen on 1/18/2021
 *
 * @author: NhanNguyen
 * @date: 1/18/2021
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    /**
     * -- GETTER --
     *
     *
     * -- SETTER --
     *
     @return the type
      * @param type the type to set
     */
    private String type;
    /**
     * -- GETTER --
     *
     *
     * -- SETTER --
     *
     @return the code
      * @param code the code to set
     */
    private String code;
    /**
     * -- GETTER --
     *
     *
     * -- SETTER --
     *
     @return the message
      * @param message the message to set
     */
    private String message;
    private Map<String, String> fieldError;
    /**
     * -- GETTER --
     *
     *
     * -- SETTER --
     *
     @return the data
      * @param data the data to set
     */
    private Object data;

    public Response(String type, String code, String message, Object data) {
        this.type = type;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(String type, String code, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
    }

    public Response(String type, String code, Object data) {
        this.type = type;
        this.code = code;
        this.data = data;
    }

    public Response(String type, String code) {
        this.type = type;
        this.code = code;
    }

    public Response(String type, String code, String message, Map<String, String> fieldError) {
        this.type = type;
        this.code = code;
        this.message = message;
        this.fieldError = fieldError;
    }

    public Response(String type) {
        this.type = type;
    }

    public Response() {
    }

    /**
     * @param data the data to set
     */
    public Response withData(Object data) {
        this.data = data;
        return this;
    }

    public static Response success(String code) {
        return new Response(Constants.RESPONSE_TYPE.SUCCESS, code);
    }

    public static Response success() {
        return new Response(Constants.RESPONSE_TYPE.SUCCESS, ErrorCodes.SUCCESS.getCode(), ErrorCodes.SUCCESS.getMessage());
    }

    public static Response error(String type, String code) {
        return new Response(type, code);
    }

    public static Response error(String type, String code, String message) {
        return new Response(type, code, message);
    }

    public static Response error(String type, String code, String message, Map<String,String> fieldErrors) {
        return new Response(type, code, message, fieldErrors);
    }

    public static Response warning(String code) {
        return new Response(Constants.RESPONSE_TYPE.WARNING, code);
    }

    public static Response invalidPermission() {
        return new Response(Constants.RESPONSE_TYPE.ERROR, "invalidPermission");
    }

    public static Response confirm(String code, String callback, Object data) {
        return new Response(Constants.RESPONSE_TYPE.CONFIRM, code, callback, data);
    }

    public static Response confirm(String code, String callback) {
        return new Response(Constants.RESPONSE_TYPE.CONFIRM, code, callback, null);
    }

    public static Response success(String code, String message) {
        return new Response(Constants.RESPONSE_TYPE.SUCCESS, code, message);
    }

    public static Response warning(String code, String message) {
        return new Response(Constants.RESPONSE_TYPE.WARNING, code, message);
    }
}