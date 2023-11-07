package nextstep.jwp.conrtroller;

import nextstep.jwp.service.LoginService;

public class LoginController {
    private String requestMethod;
    private String requestUrl;
    private LoginService loginService = new LoginService();

    public LoginController(String requestMethod, String requestUrl) {
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
    }
}
