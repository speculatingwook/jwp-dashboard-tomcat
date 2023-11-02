package org.apache.coyote.http11.httprequest;

import java.util.Arrays;

public enum HttpMethod {
    GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD;

    public static HttpMethod from(final String method) {
        return Arrays.stream(values())
                .filter(httpMethod -> httpMethod.name().equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 HTTP Method 입니다."));
    }
}
