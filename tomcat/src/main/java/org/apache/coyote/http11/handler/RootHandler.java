package org.apache.coyote.http11.handler;

import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.httprequest.HttpRequest;

import java.util.Objects;

public class RootHandler implements Handler {
    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        String responseBody = "Hello World!";
        return HttpResponse.success(responseBody);
    }

    @Override
    public boolean supports(HttpRequest httpRequest) {
        return HttpMethod.GET == httpRequest.getMethod() && Objects.equals(httpRequest.getPath(), "/");
    }
}
