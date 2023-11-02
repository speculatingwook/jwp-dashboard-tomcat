package org.apache.coyote.http11.handler;

import nextstep.jwp.db.InMemoryUserRepository;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.Utill.FileFinder;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

import java.util.Objects;

public class LoginHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        findUser(httpRequest);

        FileFinder fileFinder = new FileFinder();
        String responseBody = fileFinder.fromPath("/login.html");
        return HttpResponse.success(responseBody);
    }

    private static void findUser(HttpRequest httpRequest) {
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

    @Override
    public boolean supports(HttpRequest httpRequest) {
        return HttpMethod.GET == httpRequest.getMethod() && Objects.equals(httpRequest.getPath(), "/login");
    }
}
