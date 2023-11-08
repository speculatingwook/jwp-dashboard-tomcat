package nextstep.jwp.conrtroller;

import nextstep.jwp.service.LoginService;
import org.apache.coyote.http11.Response;

public class LoginController {
    private String requestMethod;
    private String requestUrl;
    private LoginService loginService = new LoginService();

    public LoginController(String requestMethod, String requestUrl) {
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
    }

    public Response generateResponse() {
        switch (requestMethod) {
            case "POST":
                break;
            case "GET":
                break;
            default:
                break;
        }
    }
}
