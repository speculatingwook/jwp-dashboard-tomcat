package org.apache.coyote.http11.response;

import java.util.Arrays;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    PATCH("PATCH");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static HttpMethod findMethod(String requestMethod) {
        return Arrays.stream(HttpMethod.values())
                .filter(httpMethod -> httpMethod.method.equals(requestMethod))
                .findFirst()
                .orElse(null);
    }
}

