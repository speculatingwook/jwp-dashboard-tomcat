package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod method = request.getHttpRequestLine().getMethod();
        if (method.equals(HttpMethod.POST)) {
            doPost(request, response);
        }
        if (method.equals(HttpMethod.GET)) {
            doGet(request, response);
        }
    }
    protected void doPost(HttpRequest request, HttpResponse response){};
    protected void doGet(HttpRequest request, HttpResponse response){};
}
