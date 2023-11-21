package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.Paths;
import org.apache.coyote.http11.StatusCode;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpResponseBody;
import org.apache.coyote.http11.response.HttpResponseHeader;

import java.util.NoSuchElementException;

public class InvalidRequestController extends AbstractController{
    private HttpResponseHeader header;
    private HttpResponseBody body;

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        body = HttpResponseBody.of(Paths.NOT_FOUND.createPath());
        header = new HttpResponseHeader(StatusCode.NOT_FOUND.getStatus())
                .addContentType(Paths.NOT_FOUND.getContentType())
                .addContentLength(body.getContentLength());
        response.addBody(body).addHeader(header);
    }
}
