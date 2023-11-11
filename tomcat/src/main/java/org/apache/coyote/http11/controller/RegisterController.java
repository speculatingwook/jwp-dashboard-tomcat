package org.apache.coyote.http11.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.HttpRequest;
import org.apache.coyote.http11.HttpResponse;
import org.apache.coyote.http11.enums.ContentType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class RegisterController implements Controller {

    @Override
    public void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String method = httpRequest.getMethod();
        String path = httpRequest.getRequestPath();
        System.out.println(method);
        if (method.equals("POST")) {
            handleRegisterRequest(httpRequest, httpResponse);
        } else {
            StaticController staticController = new StaticController();
            staticController.getStaticResourceFile(path + ".html", httpResponse);
        }

    }

    private void handleRegisterRequest(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> body = httpRequest.getBody();
        User user = new User(body.get("account"), body.get("password"), body.get("email"));
        System.out.println(user);
        InMemoryUserRepository.save(user);
        httpResponse.setStatusCode(302);
        httpResponse.setContentType(ContentType.HTML);
        httpResponse.addHeader("Location", "/index.html");// HTTP 상태코드 200 (OK)
    }
}
