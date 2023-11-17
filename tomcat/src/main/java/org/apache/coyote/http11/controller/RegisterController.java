package org.apache.coyote.http11.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.HttpRequest;
import org.apache.coyote.http11.HttpResponse;
import org.apache.coyote.http11.enums.ContentType;

import java.io.IOException;
import java.util.Map;

public class RegisterController extends AbstractController {

    @Override
    public void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String method = httpRequest.getMethod();
        String path = httpRequest.getRequestPath();
        if (method.equals("POST")) {
            handleRegisterRequest(httpRequest, httpResponse);
        } else {
            getStaticResourceFile(path + ".html", httpResponse);
        }

    }

    private void handleRegisterRequest(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> body = httpRequest.getBody();
        User user = new User(body.get("account"), body.get("password"), body.get("email"));
        System.out.println(user);
        InMemoryUserRepository.save(user);
        redirectToHome("/index.html",httpResponse);
    }
}
