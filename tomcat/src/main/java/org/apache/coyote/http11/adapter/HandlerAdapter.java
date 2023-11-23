package org.apache.coyote.http11.adapter;

import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;
import org.apache.coyote.http11.view.ModelAndView;

public interface HandlerAdapter {
    ModelAndView handle(HttpRequest request, HttpResponse response, Object handler);

    boolean supports(Object httpRequest);

}
