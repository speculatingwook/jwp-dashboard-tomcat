package nextstep.jwp;

import nextstep.jwp.controller.CSSController;
import nextstep.jwp.controller.Controller;
import nextstep.jwp.controller.IndexController;
import nextstep.jwp.controller.JSController;
import nextstep.jwp.controller.LoginController;
import nextstep.jwp.controller.RegisterController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private Map<String, Controller> handlerMapping;

    public RequestMapping() {
        this.handlerMapping = new HashMap<>();

        handlerMapping.put("/css", new CSSController());
        handlerMapping.put("/js", new JSController());
        handlerMapping.put("/", new IndexController());
        handlerMapping.put("/index", new IndexController());
        handlerMapping.put("/index.html", new IndexController());

        handlerMapping.put("/login", new LoginController());

        handlerMapping.put("/register", new RegisterController());
    }

    public Controller getController(String url){
        if(url.contains(".css")){
            return handlerMapping.get("/css");
        }
        if(url.contains(".js")){
            return handlerMapping.get("/js");
        }
        return handlerMapping.get(url);
    }
}
