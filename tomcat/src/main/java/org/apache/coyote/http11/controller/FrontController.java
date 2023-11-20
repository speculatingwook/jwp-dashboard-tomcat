package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.request.HttpRequestHeader;
import org.apache.coyote.http11.response.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController implements Controller{
    private final Map<String, Controller> controllers;

    public FrontController() {
        controllers = new HashMap<>();
        controllers.put("/login", new LoginController());
        controllers.put("/register", new RegisterController());
    }

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        HttpRequestHeader httpRequestHeader = httpRequest.getHttpRequestHeader();

        String path = httpRequestHeader.getPath();
        Controller controller = null;

        for (String key : controllers.keySet()) {
            if (path.startsWith(key)) {
                controller = controllers.get(key);
            }
        }

        if (controller == null) {
            controller = new StaticResourceController();
        }

        return controller.handleRequest(httpRequest, httpResponse);
    }
}
