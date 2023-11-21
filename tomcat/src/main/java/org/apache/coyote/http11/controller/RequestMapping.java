package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.Paths;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private static Map<String, Controller> controllers =
            new HashMap<String, Controller>();

    static {
        controllers.put("/register.html", new CreateUserController());
        controllers.put("/login.html", new LoginController());
        controllers.put("/index.html", new StaticContentController());
        controllers.put("invalidRequest", new InvalidRequestController());
    }

    public static Controller getController(String requestUrl) {
        if (requestUrl.endsWith(".js") || requestUrl.endsWith(".css") || requestUrl.endsWith(".html") || requestUrl.endsWith("svg")) {
            return new StaticContentController();
        }
        if (requestUrl.contains("login")) {
            return new LoginController();
        }
        if (requestUrl.contains("index")) {
            return new StaticContentController();
        }
        if (controllers.containsKey(requestUrl)) {
            return controllers.get(requestUrl);
        }
        return controllers.get("invalidRequest");
    }

}
