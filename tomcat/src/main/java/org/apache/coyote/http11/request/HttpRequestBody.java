package org.apache.coyote.http11.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HttpRequestBody {
    private String body;

    @Builder
    public HttpRequestBody(String body) {
        this.body = body;
    }

}
