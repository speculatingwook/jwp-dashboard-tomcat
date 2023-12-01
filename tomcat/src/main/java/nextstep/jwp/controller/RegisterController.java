package nextstep.jwp.controller;

import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.ContentType;
import org.apache.coyote.response.HttpResponse;
import org.apache.coyote.response.HttpStatus;

import java.io.IOException;

import static nextstep.util.Constant.REGISTER_VIEW_PATH;

public class RegisterController implements Controller {

    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.sendRedirect(REGISTER_VIEW_PATH);
    }
}
