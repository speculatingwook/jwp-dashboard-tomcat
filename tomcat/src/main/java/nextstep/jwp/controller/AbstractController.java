package nextstep.jwp.controller;

import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.request.startline.HttpMethod;
import org.apache.coyote.response.HttpResponse;

import java.net.URISyntaxException;

import static org.apache.coyote.request.startline.HttpMethod.*;

public abstract class AbstractController implements Controller {

    @Override
    public HttpResponse service(HttpRequest request) throws URISyntaxException {
        final HttpMethod method = request.getRequestMethod();

        if (method.equals(POST)) {
            return doPost(request);
        }
        return doGet(request);
    }

    protected abstract HttpResponse doPost(HttpRequest request) throws URISyntaxException;
    protected abstract HttpResponse doGet(HttpRequest request) throws URISyntaxException;
}
