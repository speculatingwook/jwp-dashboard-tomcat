package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private static final Logger log = LoggerFactory.getLogger(FrontController.class);
    private final Map<String, Controller> controllers;

    public FrontController() {
        controllers = new HashMap<>();
        controllers.put("/index", new IndexController());
        controllers.put("/login", new LoginController());
        controllers.put("/register", new RegisterController());
        controllers.put("/assets", new StaticResourceController());
        controllers.put("/js", new StaticResourceController());
        controllers.put("/css", new StaticResourceController());
    }

    public HttpResponse processRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String path = httpRequest.getPath();
        Controller controller = null;

        for (String key : controllers.keySet()) {
            if (path.startsWith(key)) {
                controller = controllers.get(key);
            }
        }

        if (controller != null) {
            httpResponse = controller.handleRequest(httpRequest, httpResponse);
        } else {
            log.warn("No controller found for path: {}", path);
            httpResponse = new HttpResponse()
                    .statusCode(HttpStatusCode.NOT_FOUND.getCode())
                    .statusMessage(HttpStatusCode.NOT_FOUND.getMessage())
                    .addHeader("Content-Type", "text/html;charset=utf-8")
                    .addHeader("Content-Length", String.valueOf(0))
                    .body("<html><body><h1>Hello world!</h1></body></html>")
                    .build();
        }
        return httpResponse;
    }
}
