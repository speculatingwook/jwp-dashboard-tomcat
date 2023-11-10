package nextstep.jwp.controller;

import java.util.Map;
import nextstep.jwp.service.LoginService;
import org.apache.coyote.http11.handler.Controller;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public class LoginController implements Controller {

    private final LoginService loginService;

    public LoginController() {
        loginService = LoginService.getInstance();
    }

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        Map<String, String> body = request.bodyToMap();
        String account = body.get("account");
        String password = body.get("password");
        if (account == null || password == null) {
            return "redirect:/401.html";
        }
        try {
            loginService.findUser(account, password);
        } catch (Exception e) {
            return "redirect:/401.html";
        }
        return "redirect:/index.html";
    }

}
