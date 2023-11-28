package nextstep.jwp.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import nextstep.util.Constant;
import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.ContentType;
import org.apache.coyote.response.HttpResponse;
import org.apache.coyote.response.HttpStatus;
import org.apache.coyote.session.Session;

import java.io.IOException;

public class RegisterFormController implements Controller {

    @Override
    public HttpResponse execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Session session = httpRequest.getSession();
        String account = (String) session.getAttribute("account");
        String password = (String) session.getAttribute("password");
        String email = (String) session.getAttribute("email");
        InMemoryUserRepository.save(new User(1L, account, password, email));

        return forwardProcess(httpResponse, HttpStatus.CREATED, Constant.INDEX_VIEW_PATH, ContentType.from(Constant.INDEX_VIEW_PATH));
    }
    private HttpResponse forwardProcess(HttpResponse httpResponse, HttpStatus httpStatus, String viewPath, ContentType contentType) throws IOException {
        httpResponse.setHttpStatus(httpStatus);
        httpResponse.setViewPath(viewPath);
        httpResponse.setContentType(contentType);
        return httpResponse.makeResponse();
    }
}
