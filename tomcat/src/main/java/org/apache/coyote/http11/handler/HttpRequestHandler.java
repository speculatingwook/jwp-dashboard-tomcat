package org.apache.coyote.http11.handler;

import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public interface HttpRequestHandler {
    void handleRequest(HttpRequest request, HttpResponse response);
}
