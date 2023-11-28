package nextstep.jwp;

import nextstep.jwp.controller.StaticController;
import nextstep.jwp.controller.Controller;
import nextstep.jwp.controller.ExceptionController;
import nextstep.jwp.controller.IndexController;
import nextstep.jwp.controller.LoginController;
import nextstep.jwp.controller.LoginFormController;
import nextstep.jwp.controller.RegisterController;
import nextstep.jwp.controller.RegisterFormController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private Map<String, Controller> getHandlers;
    private Map<String, Controller> postHandlers;
    public RequestMapping() {
        this.getHandlers = new HashMap<>();
        this.postHandlers = new HashMap<>();

        initGetHandler();
        initPostHandler();
    }

    private void initPostHandler() {
        postHandlers.put("/register", new RegisterFormController());
        postHandlers.put("/login", new LoginFormController());
    }

    private void initGetHandler() {
        getHandlers.put("/static", new StaticController());
        getHandlers.put("/", new IndexController());
        getHandlers.put("/index", new IndexController());
        getHandlers.put("/index.html", new IndexController());
        getHandlers.put("/login", new LoginController());
        getHandlers.put("/register", new RegisterController());
    }

    public Controller getController(String method, String url){
        System.out.println();
        System.out.println("method = " + method);
        System.out.println("url = " + url);
        System.out.println();
        if(url.contains(".css") || url.contains(".js")){
            return getHandlers.get("/static");
        }
        if(method.trim().equals("GET")){
            return getHandlers.get(url);
        }
        if(method.trim().equals("POST")){
            return postHandlers.get(url);
        }
        return new ExceptionController();
    }
}
