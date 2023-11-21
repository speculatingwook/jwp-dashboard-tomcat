package nextstep.jwp.controller;

import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.ContentType;
import org.apache.coyote.response.HttpResponse;
import org.apache.coyote.response.HttpStatus;

import java.io.IOException;

public class CSSController implements Controller{
    private static String CSS_VIEW_NAME = "";
    @Override
    public HttpResponse execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        CSS_VIEW_NAME = httpRequest.getPath();
        httpResponse.setViewPath(CSS_VIEW_NAME);
        httpResponse.setContentType(ContentType.from(CSS_VIEW_NAME));
        httpResponse.setHttpStatus(HttpStatus.OK);
        return httpResponse.makeResponse();
    }
}
