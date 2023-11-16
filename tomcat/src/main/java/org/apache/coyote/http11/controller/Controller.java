package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;

public interface Controller {
    void handleRequest(HttpRequest httpRequest);
}
