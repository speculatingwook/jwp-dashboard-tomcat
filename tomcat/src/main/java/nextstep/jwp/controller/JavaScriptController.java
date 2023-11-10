package nextstep.jwp.controller;

import org.apache.coyote.http11.handler.Controller;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public class JavaScriptController implements Controller {

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        return request.getPath();
    }
}
