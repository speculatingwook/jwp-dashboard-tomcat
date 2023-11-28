package nextstep.jwp.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.response.ContentType;
import org.apache.coyote.response.HttpResponse;
import org.apache.coyote.response.HttpStatus;
import org.apache.coyote.session.Session;

import java.io.IOException;
import java.util.Optional;

import static nextstep.util.Constant.ERROR_401_VIEW_PATH;
import static nextstep.util.Constant.INDEX_VIEW_PATH;
import static org.apache.coyote.response.ContentType.from;

public class LoginFormController implements Controller{
    @Override
    public HttpResponse execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String account = httpRequest.getParameter("account");
        Optional<User> user = InMemoryUserRepository.findByAccount(account);

        //fail
        if (user.isEmpty()) {
            return forwardProcess(httpResponse, HttpStatus.UNAUTHORIZED, ERROR_401_VIEW_PATH, from(ERROR_401_VIEW_PATH),null);
        }
        Session session = httpRequest.getSession();
        session.setAttribute("user", user);
        //success
        return forwardProcess(httpResponse, HttpStatus.FOUND, INDEX_VIEW_PATH, from(INDEX_VIEW_PATH),"JSESSIONID=" + session.getSessionId());
    }

    private HttpResponse forwardProcess(HttpResponse httpResponse, HttpStatus httpStatus, String viewPath, ContentType contentType, String cookie) throws IOException {
        httpResponse.setHttpStatus(httpStatus);
        httpResponse.setViewPath(viewPath);
        httpResponse.setContentType(contentType);
        httpResponse.setCookie(cookie);
        return httpResponse.makeResponse();
    }
}
