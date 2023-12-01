package org.apache.coyote.http11.HTTPRequest;

import java.util.Arrays;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    OPTIONS;

    public static HttpMethod find(String method) {
        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(method))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Request Method " + method + " is unsupported"));
    }

}
