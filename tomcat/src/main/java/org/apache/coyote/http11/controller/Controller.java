package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;

import java.io.OutputStream;

public interface Controller {
    void handleRequest(HttpRequest httpRequest, OutputStream outputStream);
}
