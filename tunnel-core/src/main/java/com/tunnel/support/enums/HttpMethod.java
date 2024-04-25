package com.tunnel.support.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

/**
 * @author yq
 * @since 2024/4/16
 */
@Getter
@RequiredArgsConstructor
public enum HttpMethod {

    /**
     * Post请求
     */
    POST("Post"),
    /**
     * Get请求
     */
    GET("Get"),
    /**
     * Delete请求
     */
    DELETE("Delete"),
    /**
     * Put请求
     */
    PUT("Put"),

    ;
    private final String code;

    public static HttpMethod of(String code) {
        return Stream.of(values()).filter(c -> c.code.equals(code)).findFirst().orElse(null);
    }

    public static boolean isPost(String code) {
        return POST.code.equals(code);
    }

    public static boolean isGet(String code) {
        return GET.code.equals(code);
    }

    public static boolean isDelete(String code) {
        return DELETE.code.equals(code);
    }

    public static boolean isPut(String code) {
        return PUT.code.equals(code);
    }
}
