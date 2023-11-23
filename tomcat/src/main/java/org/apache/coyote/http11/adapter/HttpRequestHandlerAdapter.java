package org.apache.coyote.http11.adapter;

import org.apache.coyote.http11.handler.HttpRequestHandler;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;
import org.apache.coyote.http11.view.ModelAndView;

public class HttpRequestHandlerAdapter implements HandlerAdapter {
    @Override
    public ModelAndView handle(HttpRequest request, HttpResponse response, Object handler) {
        HttpRequestHandler requestHandler = (HttpRequestHandler) handler;
        requestHandler.handleRequest(request, response);
        return null;
    }

    @Override
    public boolean supports(Object httpRequest) {
        return (httpRequest instanceof HttpRequestHandler);
    }
}
