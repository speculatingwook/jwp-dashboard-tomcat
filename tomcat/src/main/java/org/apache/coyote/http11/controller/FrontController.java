package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.HTTPRequest.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private static final Logger log = LoggerFactory.getLogger(FrontController.class);
    private final Map<String, Controller> controllerMap;
    public FrontController() {
        controllerMap = new HashMap<>();
        controllerMap.put("/login", new LoginController());
        controllerMap.put("/register", new RegisterController());
    }

    public Controller match(HttpRequest httpRequest) {
        Controller controller = controllerMap.get(httpRequest.getRequestPath().getPath());
        if (controller != null)
            return controller;
        return new StaticController();
    }
}
