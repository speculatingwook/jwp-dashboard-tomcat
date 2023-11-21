package nextstep.jwp.controller;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.request.RequestParam;
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
        RequestParam requestParam = httpRequest.getRequestParam();

        if (requestParam.get("Cookie")==null) {
            return forwardProcess(httpResponse, HttpStatus.OK, INDEX_VIEW_PATH, from(INDEX_VIEW_PATH), null);
        }

        //queryString이 있다 = 로그인 폼 요청
        if (requestParam.hasRequestParam()) {
            return loginFormProcess(httpResponse, requestParam);
        }

        //queryString이 없다 = 로그인 페이지 포워딩
        return forwardProcess(httpResponse, HttpStatus.FOUND, LOGIN_VIEW_PATH, from(LOGIN_VIEW_PATH),null);
    }

    private HttpResponse loginFormProcess(HttpResponse httpResponse, RequestParam requestParam) throws IOException {
        String account = requestParam.get("account");
        Optional<User> user = InMemoryUserRepository.findByAccount(account);

        //fail
        if (user.isEmpty()) {
            return forwardProcess(httpResponse, HttpStatus.UNAUTHORIZED, ERROR_401_VIEW_PATH, from(ERROR_401_VIEW_PATH),null);
        }

        String cookie = "JSESSIONID=656cef62-e3c4-40bc-a8df-94732920ed46";
        //success
        return forwardProcess(httpResponse, HttpStatus.FOUND, INDEX_VIEW_PATH, from(INDEX_VIEW_PATH),cookie);
    }

    private HttpResponse forwardProcess(HttpResponse httpResponse, HttpStatus httpStatus, String viewPath, ContentType contentType, String cookie) throws IOException {
        httpResponse.setHttpStatus(httpStatus);
        httpResponse.setViewPath(viewPath);
        httpResponse.setContentType(contentType);
        httpResponse.setCookie(cookie);
        return httpResponse.makeResponse();
    }
}
