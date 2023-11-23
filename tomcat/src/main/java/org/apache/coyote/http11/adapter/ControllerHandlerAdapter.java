package org.apache.coyote.http11.adapter;

import org.apache.coyote.http11.handler.Controller;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;
import org.apache.coyote.http11.view.ModelAndView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller);
    }

    @Override
    public ModelAndView handle(HttpRequest request, HttpResponse response, Object handler) {

        Controller controller = (Controller) handler;

        String viewName = controller.process(request, response);
        if (viewName == null) {
            return null;
        }
        return new ModelAndView(viewName);
    }
}
