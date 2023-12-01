package org.apache.coyote.http11.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.HTTPRequest.HttpMethod;
import org.apache.coyote.http11.HTTPRequest.HttpRequest;
import org.apache.coyote.http11.HTTPRequest.HttpRequestPath;
import org.apache.coyote.http11.HTTPResponse.ContentType;
import org.apache.coyote.http11.HTTPResponse.HttpResponse;
import org.apache.coyote.http11.HTTPResponse.HttpStatusCode;

import java.io.IOException;
import java.util.Map;

public class RegisterController extends AbstractController {

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
        HttpMethod method = httpRequest.getMethod();
        HttpRequestPath requestPath = httpRequest.getRequestPath();
        if (method.equals(HttpMethod.POST)) {
            return handleRegisterRequest(httpRequest);
        } else {
            return getStaticResourceFile(requestPath.getPath() + ".html");
        }

    }

    private HttpResponse handleRegisterRequest(HttpRequest httpRequest) {
        Map<String, String> body = httpRequest.getRequestBody().getBody();
        User user = new User(body.get("account"), body.get("password"), body.get("email"));
        System.out.println(user);
        InMemoryUserRepository.save(user);
        generateRedirection("/index.html");
        return generateHTTPResponse(ContentType.HTML, HttpStatusCode.FOUND);
    }
}
