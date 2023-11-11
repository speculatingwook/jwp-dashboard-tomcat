package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.HttpRequest;
import org.apache.coyote.http11.HttpResponse;

import java.io.IOException;

public interface Controller {
    void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
