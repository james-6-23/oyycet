package com.cet.practice.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<T>(0, "OK", data);
    }

    public static <T> Result<T> badRequest(String message) {
        return new Result<T>(400, message, null);
    }

    public static <T> Result<T> unauthorized(String message) {
        return new Result<T>(401, message, null);
    }

    public static <T> Result<T> forbidden(String message) {
        return new Result<T>(403, message, null);
    }

    public static <T> Result<T> notFound(String message) {
        return new Result<T>(404, message, null);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<T>(code, message, null);
    }
}

