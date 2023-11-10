package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.HttpRequest;
import org.apache.coyote.http11.HttpResponse;
import org.apache.coyote.http11.enums.ContentType;

import java.io.IOException;

public class NotFoundController implements Controller{
    @Override
    public void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String requestPath = httpRequest.getRequestPath();

        if(requestPath.equals("/")) {
            httpResponse.setResponseBody("Hello world!");
            httpResponse.setContentType(ContentType.HTML);
            httpResponse.setStatusCode(200);
        } else {
            httpResponse.setResponseBody("Unidentified path");
            httpResponse.setContentType(ContentType.HTML);
            httpResponse.setStatusCode(404);
        }
    }
}
