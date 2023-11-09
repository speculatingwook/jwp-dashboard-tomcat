package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MappingController {

    private static final Logger log = LoggerFactory.getLogger(MappingController.class);
    private Map<String, Controller> controllerMap;
    public MappingController() {
        controllerMap = new HashMap<>();
        controllerMap.put("/login", new LoginController());
    }

    public Controller match(HttpRequest httpRequest) {
        String path = httpRequest.getRequestPath();
        if (path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".html")) {
            return new StaticController();
        }
        Controller controller = controllerMap.get(httpRequest.getRequestPath());
        if (controller != null)
            return controller;
        return new NotFoundController();
    }
}
