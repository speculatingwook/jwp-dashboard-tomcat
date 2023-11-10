package nextstep.jwp.controller;

import org.apache.coyote.http11.handler.Controller;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public class RootController implements Controller {

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        response.setBody("Hello World!");
        return null;
    }

}
