package org.apache.coyote.http11;

import java.util.Arrays;

public enum HttpMethod {
    GET, POST, DELETE, HEAD, PATCH, PUT, NONE;

    public static HttpMethod of(String method) {
        return Arrays.stream(HttpMethod.values())
                .filter(e -> e.toString().equals(method))
                .findAny()
                .orElse(NONE);
    }
}