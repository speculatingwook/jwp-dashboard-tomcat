package org.apache.coyote.http11.handler;

import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.Utill.FileFinder;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller);
    }

    @Override
    public HttpResponse handle(HttpRequest request, HttpResponse response, Object handler) {
        Controller controller = (Controller) handler;

        String viewName = controller.process(request, response);
        if (viewName == null) {
            return response;
        }

        if (viewName.startsWith("redirect:")) {
            response.sendRedirect(viewName.substring(9));
            return response;
        }

        FileFinder fileFinder = new FileFinder();
        try {
            String fileContent = fileFinder.fromPath(viewName);
            response.setBody(fileContent);
        } catch (Exception e) {
            response.sendError(HttpStatus.NOT_FOUND, "Not Found");
        }
        return response;
    }
}
