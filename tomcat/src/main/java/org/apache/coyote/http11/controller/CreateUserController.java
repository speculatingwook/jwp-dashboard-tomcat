package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.Paths;
import org.apache.coyote.http11.StatusCode;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpResponseBody;
import org.apache.coyote.http11.response.HttpResponseHeader;

public class CreateUserController extends AbstractController {
    private HttpResponseHeader header;
    private HttpResponseBody body;
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        if (request.getHttpRequestLine().getPath().equals("/register")) {
            body = HttpResponseBody.of(Paths.REGISTER.createPath());
            header = new HttpResponseHeader(StatusCode.FOUND.getStatus())
                    .addLocation(Paths.INDEX.getPath())
                    .addContentType(Paths.INDEX.getContentType())
                    .addContentLength(body.getContentLength());
        }
        response.addBody(body).addHeader(header);
    }

}
