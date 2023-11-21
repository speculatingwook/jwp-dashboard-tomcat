package nextstep.jwp.controller;

import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.HttpResponse;

import java.io.IOException;

public interface Controller {
    HttpResponse execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
