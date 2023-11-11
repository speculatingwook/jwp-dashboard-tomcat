package org.apache.coyote.http11.handler;

import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public interface HandlerAdapter {
    HttpResponse handle(HttpRequest request, HttpResponse response, Object handler);

    boolean supports(Object httpRequest);

}
