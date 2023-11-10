package nextstep.jwp.controller;

import java.util.Map;
import nextstep.jwp.service.UserService;
import org.apache.coyote.http11.handler.Controller;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public class RegisterController implements Controller {

    private final UserService userService;

    public RegisterController() {
        userService = UserService.getInstance();
    }

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        Map<String, String> body = request.bodyToMap();
        String account = body.get("account");
        String email = body.get("email");
        String password = body.get("password");
        if (account == null || email == null || password == null) {
            return "redirect:/401.html";
        }
        try {
            userService.registerUser(account, email, password);
        } catch (Exception e) {
            return "redirect:/401.html";
        }
        return "redirect:/index.html";
    }

}
