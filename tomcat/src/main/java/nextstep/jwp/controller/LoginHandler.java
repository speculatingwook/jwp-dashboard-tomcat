package nextstep.jwp.controller;

import nextstep.jwp.service.LoginService;
import org.apache.coyote.http11.handler.Controller;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public class LoginHandler implements Controller {

    private final LoginService loginService;

    public LoginHandler() {
        loginService = LoginService.getInstance();
    }

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        String account = request.getQueryParam("account");
        String password = request.getQueryParam("password");
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
