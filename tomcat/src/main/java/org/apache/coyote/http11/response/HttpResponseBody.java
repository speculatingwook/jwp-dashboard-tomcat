package org.apache.coyote.http11.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HttpResponseBody {
    private String body;

    @Builder
    public HttpResponseBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return body;
    }
}
