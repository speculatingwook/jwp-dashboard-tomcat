package nextstep.jwp.controller;

import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.ContentType;
import org.apache.coyote.response.HttpResponse;

import java.net.URISyntaxException;

import static org.apache.coyote.response.StatusCode.OK;

public class ResourceController extends AbstractController{

    @Override
    protected HttpResponse doPost(final HttpRequest request) throws URISyntaxException {
        return doGet(request);
    }

    @Override
    protected HttpResponse doGet(final HttpRequest request) throws URISyntaxException {
        final String requestPath = request.getRequestPath();
        return HttpResponse.of(OK, ContentType.from(requestPath), requestPath);
    }
}
