package nextstep.jwp.controller;

import nextstep.util.Constant;
import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.ContentType;
import org.apache.coyote.response.HttpResponse;
import org.apache.coyote.response.HttpStatus;

import java.io.IOException;

import static nextstep.util.Constant.ERROR_400_VIEW_PATH;
import static org.apache.coyote.response.ContentType.from;
import static org.apache.coyote.response.HttpStatus.BAD_REQUEST;

public class ExceptionController implements Controller{
    @Override
    public HttpResponse execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.setHttpStatus(BAD_REQUEST);
        httpResponse.setViewPath(ERROR_400_VIEW_PATH);
        httpResponse.setContentType(from(ERROR_400_VIEW_PATH));
        return httpResponse.makeResponse();
    }
}
