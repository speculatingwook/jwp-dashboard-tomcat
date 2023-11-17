package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.Paths;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public class GetController implements Controller {
    @Override
    public boolean canHandle(String path) {
        return Paths.containsPath(path);
    }

    @Override
    public void handleRequest(HttpRequest request, HttpResponse response) {
        String path = request.getHttpRequestLine().getPath();
        for (Paths paths : Paths.values()) {
            if (paths.getPath().equals(path)) {
                response.createResponse(path);
                return;
            }
        }
    }
}
