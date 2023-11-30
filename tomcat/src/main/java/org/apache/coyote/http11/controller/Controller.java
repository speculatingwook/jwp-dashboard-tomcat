package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.HTTPRequest.HttpRequest;
import org.apache.coyote.http11.HTTPResponse.HttpResponse;

import java.io.IOException;

public interface Controller {
    HttpResponse handleRequest(HttpRequest httpRequest) throws IOException;
}
