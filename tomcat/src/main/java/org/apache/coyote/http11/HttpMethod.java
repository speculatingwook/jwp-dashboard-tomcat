package org.apache.coyote.http11;

import org.apache.coyote.http11.exception.HttpRequestMethodNotSupportedException;

import java.util.Arrays;

public enum HttpMethod {
    GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD;

    public static HttpMethod from(final String method) {
        return Arrays.stream(values())
                .filter(httpMethod -> httpMethod.name().equals(method))
                .findFirst()
                .orElseThrow(HttpRequestMethodNotSupportedException::new);
    }
}
