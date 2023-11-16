package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private static final Logger log = LoggerFactory.getLogger(FrontController.class);
    private final Map<String, Controller> controllerMap;

    public FrontController() {
        controllerMap = new HashMap<>();
        controllerMap.put("/index.html", new IndexController());
    }

    public void processRequest(Socket connection) {
        try {
            HttpRequest httpRequest = new HttpRequest(connection.getInputStream());
            String path = httpRequest.getPath();
            Controller controller = controllerMap.get(path);

            if (controller != null) {
                controller.handleRequest(httpRequest, connection.getOutputStream());
            } else {
                log.warn("No controller found for path: {}", path);
            }
        } catch (IOException e) {
            log.error("Error processing request: {}", e.getMessage(), e);
        }
    }
}
