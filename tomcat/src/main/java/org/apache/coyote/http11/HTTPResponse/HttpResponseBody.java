package org.apache.coyote.http11.HTTPResponse;

public class HttpResponseBody {
    private String body;

    public HttpResponseBody(String body) {
        this.body = body;

    }
    public HttpResponseBody() {

    }

    public String getBody() {
        return  body;
    }
}
