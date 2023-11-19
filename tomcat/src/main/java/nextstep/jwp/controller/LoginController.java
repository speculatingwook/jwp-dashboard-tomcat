package nextstep.jwp.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.request.QueryString;
import org.apache.coyote.response.ContentType;
import org.apache.coyote.response.HttpResponse;
import org.apache.coyote.response.HttpStatus;

import java.io.IOException;
import java.util.Optional;

import static org.apache.coyote.response.ContentType.*;
import static nextstep.util.Constant.ERROR_401_VIEW_PATH;
import static nextstep.util.Constant.INDEX_VIEW_PATH;
import static nextstep.util.Constant.LOGIN_VIEW_PATH;

public class LoginController implements Controller {
    @Override
    public HttpResponse execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        QueryString queryString = httpRequest.getQueryString();

        //queryString이 있다 = 로그인 폼 요청
        if (queryString.hasQueryStrings()) {
            return loginFormProcess(httpResponse, queryString);
        }

        //queryString이 없다 = 로그인 페이지 포워딩
        return forwardProcess(httpResponse, HttpStatus.FOUND, LOGIN_VIEW_PATH, from(LOGIN_VIEW_PATH));
    }

    private HttpResponse loginFormProcess(HttpResponse httpResponse, QueryString queryString) throws IOException {
        String account = queryString.get("account");
        Optional<User> user = InMemoryUserRepository.findByAccount(account);

        //fail
        if (user.isEmpty()) {
            return forwardProcess(httpResponse, HttpStatus.UNAUTHORIZED, ERROR_401_VIEW_PATH, HTML);
        }

        //success
        return forwardProcess(httpResponse, HttpStatus.FOUND, INDEX_VIEW_PATH, from(INDEX_VIEW_PATH));
    }

    private HttpResponse forwardProcess(HttpResponse httpResponse, HttpStatus httpStatus, String viewPath, ContentType contentType) throws IOException {
        httpResponse.setHttpStatus(httpStatus);
        httpResponse.setViewPath(viewPath);
        httpResponse.setContentType(contentType);
        return httpResponse.makeResponse();
    }
}
