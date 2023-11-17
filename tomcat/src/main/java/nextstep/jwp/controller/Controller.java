package nextstep.jwp.controller;

import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.HttpResponse;

import java.net.URISyntaxException;

public interface Controller {
    HttpResponse service(HttpRequest request) throws URISyntaxException;
}
