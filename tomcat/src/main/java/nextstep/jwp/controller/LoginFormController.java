package nextstep.jwp.controller;

import nextstep.jwp.model.User;
import org.apache.coyote.http11.handler.Controller;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;
import org.apache.coyote.http11.session.Session;

public class LoginFormController implements Controller {

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        Session session = request.getSession(false);
        if (session != null) {
            Object user = session.getAttribute("user");
            if (user instanceof User) {
                return "redirect:/index.html";
            }
        }
        return "login";
    }

}
