package nextstep.jwp.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import org.apache.coyote.http11.handler.Controller;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public class LoginHandler implements Controller {

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        findUser(request);
        return "/login.html";
    }

    private void findUser(HttpRequest httpRequest) {
        String account = httpRequest.getQueryParam("account");
        String password = httpRequest.getQueryParam("password");
        if (account == null || password == null) {
            return;
        }

        InMemoryUserRepository.findByAccount(account)
                .ifPresent(user -> {
                    if (user.checkPassword(password)) {
                        System.out.println(user);
                    }
                });
    }
}
