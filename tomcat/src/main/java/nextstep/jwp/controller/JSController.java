package nextstep.jwp.controller;

import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.ContentType;
import org.apache.coyote.response.HttpResponse;
import org.apache.coyote.response.HttpStatus;

import java.io.IOException;

public class JSController implements Controller{
    private static String JS_VIEW_PATH = "";

    @Override
    public HttpResponse execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        JS_VIEW_PATH = httpRequest.getPath();
        httpResponse.setViewPath(JS_VIEW_PATH);
        httpResponse.setContentType(ContentType.from(JS_VIEW_PATH));
        httpResponse.setHttpStatus(HttpStatus.OK);
        return httpResponse.makeResponse();
    }
}
