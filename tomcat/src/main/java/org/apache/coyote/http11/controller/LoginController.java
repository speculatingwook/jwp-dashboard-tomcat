package org.apache.coyote.http11.controller;

import jakarta.servlet.http.Cookie;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.HttpRequest;
import org.apache.coyote.http11.HttpResponse;
import org.apache.coyote.http11.Session.Session;
import org.apache.coyote.http11.Session.SessionManager;
import org.apache.coyote.http11.enums.ContentType;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class LoginController extends AbstractController{
    User user;
    private final SessionManager sessionManager = new SessionManager();

    Cookie cookie;
    @Override
    public void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String method = httpRequest.getMethod();
        String path = httpRequest.getRequestPath();
        if (method.equals("POST")) {
            handleLoginRequest(httpRequest, httpResponse);
        } else {
            if(checkLogin(httpRequest)) {
                redirectToHome("/index.html",httpResponse);
            } else {
                getStaticResourceFile(path + ".html", httpResponse);
            }
        }

        httpResponse.setContentType(ContentType.HTML);
    }
    private void handleLoginRequest(HttpRequest httpRequest, HttpResponse httpResponse) {
        String account = httpRequest.getBody().get("account");
        String password = httpRequest.getBody().get("password");
        Optional<User> loginUser = authenticate(account, password);

        if (loginUser.isPresent()) {
            user = loginUser.get();
            String sessionId = generateUniqueSessionId();
            Session session = new Session(sessionId);
            session.setSession(sessionId, user);
            sessionManager.add(session);
            setCookie(httpResponse, sessionId);
            redirectToHome("/index.html",httpResponse);
        } else {
            redirectToHome("/401.html",httpResponse);
        }
    }
    private boolean checkLogin(HttpRequest httpRequest) {
        String sessionId = httpRequest.getCookie().get("JSESSIONID");
        Optional<Session> session = sessionManager.findSession(sessionId);
        if (session.isPresent()) {
            user = (User) session.get().getSession(sessionId);
        }
        return (user != null);
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

    public static String generateUniqueSessionId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private void setCookie(HttpResponse httpResponse, String sessionId) {
        System.out.println("setCookie");
        cookie = new Cookie(user.getAccount(), sessionId);
        httpResponse.addHeader("Set-Cookie", "JSESSIONID=" + cookie.getValue());
    }

}
