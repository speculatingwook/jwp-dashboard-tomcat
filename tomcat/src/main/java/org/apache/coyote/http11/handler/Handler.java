package org.apache.coyote.http11.handler;

import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public interface Handler {
    HttpResponse handle(HttpRequest httpRequest);

    boolean supports(HttpRequest httpRequest);

}
