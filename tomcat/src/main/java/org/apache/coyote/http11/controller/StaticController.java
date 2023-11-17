package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.HttpRequest;
import org.apache.coyote.http11.HttpResponse;

import java.io.IOException;
public class StaticController extends AbstractController {

    @Override
    public void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        getStaticResourceFile(httpRequest.getRequestPath(), httpResponse);
    }
}
