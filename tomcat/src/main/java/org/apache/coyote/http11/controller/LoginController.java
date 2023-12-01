package org.apache.coyote.http11.controller;

import jakarta.servlet.http.Cookie;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.HTTPRequest.HttpMethod;
import org.apache.coyote.http11.HTTPRequest.HttpRequest;
import org.apache.coyote.http11.HTTPRequest.HttpRequestPath;
import org.apache.coyote.http11.HTTPResponse.HttpResponse;
import org.apache.coyote.http11.HTTPResponse.HttpStatusCode;
import org.apache.coyote.http11.Session.Session;
import org.apache.coyote.http11.Session.SessionIdGenerator;
import org.apache.coyote.http11.Session.SessionManager;
import org.apache.coyote.http11.HTTPResponse.ContentType;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class LoginController extends AbstractController{
    private final SessionManager sessionManager = new SessionManager();
    Cookie cookie;
    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
        HttpMethod method = httpRequest.getMethod();
        HttpRequestPath requestPath = httpRequest.getRequestPath();
        if (method.equals(HttpMethod.POST)) {
            return handleLoginRequest(httpRequest);
        } else {
            if(checkLogin(httpRequest)) {
                generateRedirection("/index.html");
                return generateHTTPResponse(ContentType.HTML, HttpStatusCode.FOUND);
            } else {
                return getStaticResourceFile(requestPath.getPath()+ ".html");
            }
        }
    }
    private HttpResponse handleLoginRequest(HttpRequest httpRequest) {
        String account = httpRequest.getRequestBody().getBody().get("account");
        String password = httpRequest.getRequestBody().getBody().get("password");
        Optional<User> loginUser = authenticate(account, password);

        if (loginUser.isPresent()) {
            String sessionId = SessionIdGenerator.generateSessionId();
            Session session = new Session(sessionId);
            sessionManager.add(session);
            cookie = new Cookie(loginUser.get().getAccount(), sessionId);
            addCookie(cookie);
            generateRedirection("/index.html");
        } else {
            generateRedirection("/401.html");
        }
        return generateHTTPResponse(ContentType.HTML, HttpStatusCode.FOUND);
    }
    private boolean checkLogin(HttpRequest httpRequest) {
        String sessionId = httpRequest.getRequestHeader().getCookie().get("JSESSIONID");
        Optional<Session> session = sessionManager.findSession(sessionId);
        System.out.println(session.isPresent());
        return session.isPresent();
    }

    private Optional<User> authenticate(String account, String password) {
        Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.checkPassword(password)) {
                return Optional.of(user); // 인증 성공
            }
        }
        return Optional.empty(); // 인증 실패
    }


}
