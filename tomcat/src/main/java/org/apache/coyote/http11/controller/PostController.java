package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.Paths;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public class PostController implements Controller {
    @Override
    public boolean canHandle(String path) {
        return Paths.LOGIN.getPath().contains(path) || path.equals("/register");
    }

    @Override
    public void handleRequest(HttpRequest request, HttpResponse response) {
        String path = request.getHttpRequestLine().getPath();
        if (Paths.LOGIN.getPath().contains(path)) {
            response.login(path, request);
        } else if (path.equals("/register")) {
            response.register(path);
        }
    }


}
