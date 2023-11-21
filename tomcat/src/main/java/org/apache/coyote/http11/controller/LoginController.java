package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.Service.LoginService;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.request.HttpRequestBody;
import org.apache.coyote.http11.request.HttpRequestHeader;
import org.apache.coyote.http11.response.*;
import org.apache.coyote.http11.session.Cookie;
import org.apache.coyote.http11.session.Session;
import org.apache.coyote.http11.session.SessionManager;
import org.apache.coyote.http11.util.ParseUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

public class LoginController implements Controller{
    private static final String INDEX_PAGE_URL = "index.html";
    private static final String NOT_FOUND_PAGE_URL = "static/404.html";
    private static final String UNAUTHORIZED_PAGE_URL = "401.html";
    private static final String LOGIN_PAGE_URL = "static/login.html";
    private LoginService loginService = new LoginService();
    private Router router;

    public LoginController() {
        router = new Router();
        router.addHandler(HttpMethod.GET, "/login", this::handleGetLogin);
        router.addHandler(HttpMethod.POST, "/login", this::handlePostLogin);
    }

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
        return router.mappingEndpointHandler(httpRequest);
    }

    public HttpResponse handleGetLogin(HttpRequest httpRequest) throws IOException {
        HttpRequestHeader httpRequestHeader = httpRequest.getHttpRequestHeader();
        Optional<Cookie> optionalCookie = httpRequestHeader.getCookie();

        if(optionalCookie.isPresent()) {
            Cookie cookie = optionalCookie.get();
            String jSessionId = cookie.getJSessionId();
            if(jSessionId != null) {
                Optional<Session> optionalSession = SessionManager.findSession(jSessionId);
                if(optionalSession.isPresent()) {
                    return HttpResponse.builder()
                            .statusCode(HttpStatusCode.FOUND.getCode())
                            .statusMessage(HttpStatusCode.FOUND.getMessage())
                            .addHeader("Location", INDEX_PAGE_URL)
                            .build();
                }
            }
        }

        URL resource = getClass()
                .getClassLoader()
                .getResource(LOGIN_PAGE_URL);
        File file = new File(resource.getFile());

        String requestUri = httpRequestHeader.getPath();
        String responseBody = new String(Files.readAllBytes(file.toPath()));

        return HttpResponse.builder()
                .statusCode(HttpStatusCode.OK.getCode())
                .statusMessage(HttpStatusCode.OK.getMessage())
                .addHeader("Content-Type", ContentType.findContentTypeFromUri(requestUri).getContentType())
                .addHeader("Content-Length", String.valueOf(responseBody.getBytes().length))
                .body(responseBody)
                .build();
    }

    public HttpResponse handlePostLogin(HttpRequest httpRequest) throws UnsupportedEncodingException {
        HttpRequestBody httpRequestBody = httpRequest.getHttpRequestBody();

        String requestBody = httpRequestBody.getBody();
        Map<String, String> queryParameters = ParseUtil.parseQueryParameters(requestBody);
        String account = queryParameters.get("account");
        String password = queryParameters.get("password");

        if(loginService.login(account, password)) {
            String jSessionId = SessionManager.generateJSessionId();
            Session session = Session.createSession(jSessionId);
            SessionManager.add(session);
            Cookie cookie = new Cookie();

            return HttpResponse.builder()
                    .statusCode(HttpStatusCode.FOUND.getCode())
                    .statusMessage(HttpStatusCode.FOUND.getMessage())
                    .addHeader("Location", INDEX_PAGE_URL)
                    .addCookie("JSESSIONID", jSessionId)
                    .build();
        }

        return HttpResponse.builder()
                .statusCode(HttpStatusCode.UNAUTHORIZED.getCode())
                .statusMessage(HttpStatusCode.UNAUTHORIZED.getMessage())
                .addHeader("Location", UNAUTHORIZED_PAGE_URL)
                .build();
    }
}