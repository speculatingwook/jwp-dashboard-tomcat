package org.apache.coyote.http11.handler;

import org.apache.coyote.http11.Utill.FileFinder;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.httprequest.HttpRequest;

import java.util.Objects;

public class IndexHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        FileFinder fileFinder = new FileFinder();
        String responseBody = fileFinder.fromPath(httpRequest.getPath());
        return HttpResponse.success(responseBody);
    }

    @Override
    public boolean supports(HttpRequest httpRequest) {
        return HttpMethod.GET == httpRequest.getMethod() && Objects.equals(httpRequest.getPath(), "/index.html");
    }
}
