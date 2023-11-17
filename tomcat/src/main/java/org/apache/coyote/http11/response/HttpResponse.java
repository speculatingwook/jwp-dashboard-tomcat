package org.apache.coyote.http11.response;

import org.apache.coyote.http11.*;
import org.apache.coyote.http11.controller.Controller;
import org.apache.coyote.http11.controller.GetController;
import org.apache.coyote.http11.controller.PostController;
import org.apache.coyote.http11.login.LoginHandler;
import org.apache.coyote.http11.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private HttpResponseHeader header;
    private HttpResponseBody body;
    private Session session;
    private Cookie cookie;
    private Controller controller;

    public HttpResponse(HttpRequest request) {
        try {
            parseResponse(request);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), e);
        }
    }
    private void parseResponse(HttpRequest request) {
        HttpMethod method = request.getHttpRequestLine().getMethod();
        String path= request.getHttpRequestLine().getPath();
        this.session = new Session(request.getHttpRequestHeader().getSessionId());
        this.cookie = request.getCookie();

        if (method.equals(HttpMethod.GET)) {
            controller = new GetController();
        } else if (method.equals(HttpMethod.POST)) {
            controller = new PostController();
        } else {
            throw new UnsupportedOperationException("Unsupported method: " + method);
        }

        if (controller.canHandle(path)) {
            controller.handleRequest(request, this);
        } else {
            invalidRequest(path);
        }
    }

    public void login(String path, HttpRequest request) {
        if (Paths.LOGIN.getPath().contains(path)) {
            LoginHandler login = new LoginHandler(request.getHttpRequestBody().getValue("account"), request.getHttpRequestBody().getValue("password"));
            if (login.checkUser()) {
                body = HttpResponseBody.of(Paths.INDEX.createPath());
                header = new HttpResponseHeader(StatusCode.FOUND.getStatus())
                        .addContentType(Paths.INDEX.getContentType())
                        .addContentLength(body.getContentLength())
                        .setCookie(cookie);
            }
            else{
                body = HttpResponseBody.of(Paths.UNAUTHORIZED.createPath());
                header = new HttpResponseHeader(StatusCode.UNAUTHORIZED.getStatus())
                        .addContentType(Paths.UNAUTHORIZED.getContentType())
                        .addContentLength(body.getContentLength());
            }
        }
    }

    private void sessionLogin() {
        if (session.isSessionValid()) {
            body = HttpResponseBody.of(Paths.INDEX.createPath());
            header = new HttpResponseHeader(StatusCode.OK.getStatus())
                    .addLocation(Paths.INDEX.getPath())
                    .addContentType(Paths.INDEX.getContentType())
                    .addContentLength(body.getContentLength());
        }
    }
    public void register(String path) {
        if (path.equals("/register")) {
            body = HttpResponseBody.of(Paths.INDEX.createPath());
            header = new HttpResponseHeader(StatusCode.OK.getStatus())
                    .addContentType(Paths.INDEX.getContentType())
                    .addContentLength(body.getContentLength());
        }
    }

    public void createResponse(String path) {
        for (Paths paths : Paths.values()) {
            String convertedPath = pathConvert(path, paths.getContentType());
            if (convertedPath.equals(paths.getPath())) {
                body = HttpResponseBody.of(paths.createPath());
                header = new HttpResponseHeader(StatusCode.OK.getStatus())
                        .addContentType(paths.getContentType())
                        .addContentLength(body.getContentLength());
            }
        }
    }
    private String pathConvert(String path, String contentType) {
        if ((!path.contains(".html")) && contentType.equals("text/html")) {
            return path + ".html";
        }
        return path;
    }

    private void invalidRequest(String path) {
        if (header == null || body == null) {
            body = HttpResponseBody.of(Paths.NOT_FOUND.createPath());
            header = new HttpResponseHeader(StatusCode.NOT_FOUND.getStatus())
                    .addContentType(Paths.NOT_FOUND.getContentType())
                    .addContentLength(body.getContentLength());
            throw new NoSuchElementException("해당 페이지를 찾을 수 없습니다: " + path);
        }
    }

    public String getResponse() {
        return String.join("\r\n", header.getHeaders(), "", body.getBodyContext());
    }

}
