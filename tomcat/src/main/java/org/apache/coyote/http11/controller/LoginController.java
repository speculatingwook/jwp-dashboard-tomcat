package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.Service.LoginService;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.HttpMethod;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpStatusCode;
import org.apache.coyote.http11.session.Cookie;
import org.apache.coyote.http11.session.Session;
import org.apache.coyote.http11.session.SessionUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LoginController implements Controller{
    private static final String INDEX_PAGE_URL = "index.html";
    private static final String NOT_FOUND_PAGE_URL = "static/404.html";
    private static final String UNAUTHORIZED_PAGE_URL = "401.html";
    private static final String LOGIN__PAGE_URL = "static/login.html";
    private LoginService loginService = new LoginService();
    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String requestUri = httpRequest.getPath();
        String httpMethod = httpRequest.getMethod();


        if(httpMethod.equals(HttpMethod.GET.getMethod())) {
            Cookie cookie = httpRequest.getCookie();
            String jSessionId = cookie.getValue("JSESSIONID");
            Optional<Session> session = SessionUtil.findSession(jSessionId);
            if (session.isPresent()) {
                return new HttpResponse()
                        .statusCode(HttpStatusCode.FOUND.getCode())
                        .statusMessage(HttpStatusCode.FOUND.getMessage())
                        .addHeader("Location", INDEX_PAGE_URL)
                        .build();
            }

            URL resource = getClass()
                    .getClassLoader()
                    .getResource(LOGIN__PAGE_URL);
            File file = new File(resource.getFile());

            String responseBody = new String(Files.readAllBytes(file.toPath()));

            return httpResponse = new HttpResponse()
                    .statusCode(HttpStatusCode.OK.getCode())
                    .statusMessage(HttpStatusCode.OK.getMessage())
                    .addHeader("Content-Type", ContentType.findContentTypeFromUri(requestUri).getContentType())
                    .addHeader("Content-Length", String.valueOf(responseBody.getBytes().length))
                    .body(responseBody)
                    .build();

        } else if(httpMethod.equals(HttpMethod.POST.getMethod())) {
            String requestBody = httpRequest.getBody();
            Map<String, String> queryParameters = parseQueryParameters(requestBody);
            String account = queryParameters.get("account");
            String password = queryParameters.get("password");

            if(loginService.login(account, password)) {
                String jSessionId = SessionUtil.generateJSessionId();
                Session session = Session.createSession(jSessionId);
                SessionUtil.add(session);

                httpResponse = new HttpResponse()
                        .statusCode(HttpStatusCode.FOUND.getCode())
                        .statusMessage(HttpStatusCode.FOUND.getMessage())
                        .addHeader("Location", INDEX_PAGE_URL)
                        .build();

                httpResponse.addCookie("JSESSIONID", jSessionId);

                return httpResponse;
            }
            return httpResponse = new HttpResponse()
                    .statusCode(HttpStatusCode.UNAUTHORIZED.getCode())
                    .statusMessage(HttpStatusCode.UNAUTHORIZED.getMessage())
                    .addHeader("Location", UNAUTHORIZED_PAGE_URL)
                    .build();
        }

        return httpResponse = new HttpResponse()
                        .statusCode(HttpStatusCode.NOT_FOUND.getCode())
                        .statusMessage(HttpStatusCode.NOT_FOUND.getMessage())
                        .addHeader("Location", NOT_FOUND_PAGE_URL)
                        .build();
    }

    public Map<String, String> parseQueryParameters(String query) throws UnsupportedEncodingException {
        query = query.replace("\r\n", "");
        String[] params = query.split("&");

        Map<String, String> queryParameters = new HashMap<>();

        for (String param : params) {
            String[] keyValue = param.split("=");

            if (keyValue.length == 2) {
                String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                queryParameters.put(key, value);
            }
        }
        return queryParameters;
    }
}


