package nextstep.jwp.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.session.Session;
import org.apache.coyote.response.ContentType;
import org.apache.coyote.response.HttpResponse;
import org.apache.coyote.response.HttpStatus;
import org.apache.coyote.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import static org.apache.coyote.response.ContentType.*;
import static nextstep.util.Constant.ERROR_401_VIEW_PATH;
import static nextstep.util.Constant.INDEX_VIEW_PATH;
import static nextstep.util.Constant.LOGIN_VIEW_PATH;

public class LoginController implements Controller {

    @Override
    public HttpResponse execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Session session = httpRequest.getSession();

        //세션에 값이 바인딩 되어 있으면 = 로그인 되어 있음 = 리다이렉팅
        if (session.getAttribute("user") != null) {
            return forwardProcess(httpResponse, HttpStatus.OK, INDEX_VIEW_PATH, from(INDEX_VIEW_PATH), null);
        }

        //queryString이 없다 = 로그인 페이지 포워딩
        return forwardProcess(httpResponse, HttpStatus.FOUND, LOGIN_VIEW_PATH, from(LOGIN_VIEW_PATH),null);
    }


    private HttpResponse forwardProcess(HttpResponse httpResponse, HttpStatus httpStatus, String viewPath, ContentType contentType, String cookie) throws IOException {
        httpResponse.setHttpStatus(httpStatus);
        httpResponse.setViewPath(viewPath);
        httpResponse.setContentType(contentType);
        httpResponse.setCookie(cookie);
        return httpResponse.makeResponse();
    }
}
