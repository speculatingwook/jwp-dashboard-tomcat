package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.HTTPRequest.HttpRequest;
import org.apache.coyote.http11.HTTPResponse.HttpResponse;

import java.io.IOException;
public class StaticController extends AbstractController {
    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
       return getStaticResourceFile(httpRequest.getRequestPath().getPath());
    }
}
