package org.apache.coyote.http11.handler;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.Utill.FileFinder;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpMethod;
import org.apache.coyote.http11.httprequest.HttpRequest;

import java.util.Objects;
import java.util.Optional;

public class LoginHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        String account = httpRequest.getQueryParam("account");
        if (account != null) {
            String password = httpRequest.getQueryParam("password");
            if (password != null) {
                Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    if (user.checkPassword(password)) {
                        System.out.println(user);
                    }
                }
            }
        }

        FileFinder fileFinder = new FileFinder();
        String responseBody = fileFinder.fromPath("/login.html");
        return HttpResponse.success(responseBody);
    }

    @Override
    public boolean supports(HttpRequest httpRequest) {
        return HttpMethod.GET == httpRequest.getMethod() && Objects.equals(httpRequest.getPath(), "/login");
    }
}
