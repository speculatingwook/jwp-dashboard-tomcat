package nextstep.jwp.controller;

import jakarta.servlet.http.HttpServletResponseWrapper;
import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.session.Session;
import org.apache.coyote.response.ContentType;
import org.apache.coyote.response.HttpResponse;
import org.apache.coyote.response.HttpStatus;

import java.io.IOException;

import static org.apache.coyote.response.ContentType.*;
import static nextstep.util.Constant.INDEX_VIEW_PATH;
import static nextstep.util.Constant.LOGIN_VIEW_PATH;

public class LoginController implements Controller {

    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Session session = httpRequest.getSession();
        //세션에 값이 바인딩 되어 있으면 = 로그인 되어 있음 = 리다이렉팅
        if (session.getAttribute("user") != null) {
            httpResponse.sendRedirect(INDEX_VIEW_PATH);
        }

        //queryString이 없다 = 로그인 페이지 포워딩
        httpResponse.setHttpStatus(HttpStatus.FOUND);
        httpResponse.sendRedirect(LOGIN_VIEW_PATH);
    }
}
