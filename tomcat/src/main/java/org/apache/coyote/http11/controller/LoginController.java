package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.Cookie;
import org.apache.coyote.http11.HttpSession;
import org.apache.coyote.http11.Paths;
import org.apache.coyote.http11.StatusCode;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpResponseBody;
import org.apache.coyote.http11.response.HttpResponseHeader;


public class LoginController extends AbstractController {
    private HttpResponseHeader header;
    private HttpResponseBody body;

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        String path = request.getHttpRequestLine().getPath();
        to(pathConvert(path));
        response.addBody(body).addHeader(header);
    }
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        HttpSession session = request.getSession();
        LoginHandler login = new LoginHandler(request.getHttpRequestBody().getValue("account"), request.getHttpRequestBody().getValue("password"));
        if (login.checkUser()) {
            Cookie newCookie = request.getCookie();
            newCookie.setJSessionId();
            response.sendRedirect(Paths.INDEX.createPath());
        }
        else {
            body = HttpResponseBody.of(Paths.UNAUTHORIZED.createPath());
            header = new HttpResponseHeader(StatusCode.UNAUTHORIZED.getStatus())
                        .addContentType(Paths.UNAUTHORIZED.getContentType())
                        .addContentLength(body.getContentLength());
        }
        response.addBody(body).addHeader(header);

    }

    public void to(String path) {
        for (Paths paths : Paths.values()) {
            if (path.equals(paths.getPath())) {
                body = HttpResponseBody.of(paths.createPath());
                header = new HttpResponseHeader(StatusCode.OK.getStatus())
                        .addContentType(paths.getContentType())
                        .addContentLength(body.getContentLength());
            }
        }
    }
    private static String pathConvert(String path) {
        for (Paths paths : Paths.values()) {
            if (paths.getPath().contains(path)) {
                if ((!path.contains(".html")) && paths.getContentType().equals("text/html")) {
                    return path + ".html";
                }
            }
        }
        return path;
    }
}
