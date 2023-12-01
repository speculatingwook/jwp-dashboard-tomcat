package org.apache.coyote.request;

import nextstep.util.Constant;
import org.apache.coyote.session.Cookie;
import org.apache.coyote.session.Session;
import org.apache.coyote.session.SessionManager;

import javax.naming.directory.NoSuchAttributeException;
import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private HttpRequestBody requestBody;
    private HttpRequestLine httpRequestLine;
    private HttpRequestHeader header;
    private RequestParam requestParam;
    private Session session;
    private Cookie cookie;


    public static HttpRequest parse(BufferedReader br) throws NoSuchAttributeException, IOException {
        HttpRequestLine requestLine = HttpRequestLine.parse(br);
        HttpRequestHeader header = HttpRequestHeader.parse(br);
        HttpRequestBody body = HttpRequestBody.parseBody(br, header.getHeader(Constant.CONTENT_LENGTH));
        RequestParam params = RequestParam.from(requestLine, body);
        Cookie httpCookie = Cookie.parseCookie(header.getHeader(Constant.COOKIE));
        Session httpSession = SessionManager.getSession(httpCookie.getCookie(Constant.JSEESIONID));
        return new HttpRequest(requestLine, header, body, params, httpSession, httpCookie);
    }

    private HttpRequest(HttpRequestLine httpRequestLine, HttpRequestHeader header, HttpRequestBody requestBody, RequestParam param, Session session, Cookie cookie) {
        this.httpRequestLine = httpRequestLine;
        this.header = header;
        this.requestBody = requestBody;
        this.requestParam = param;
        this.session = session;
        this.cookie = cookie;
    }

    public String getHttpMethod() {
        return httpRequestLine.getHttpMethod();
    }

    public String getPath() {
        return httpRequestLine.getPath();
    }

    public Session getSession() {
        if (this.session == null) {
            this.session = SessionManager.createSession();
        }
        return session;
    }

    public Session getSession(boolean create) {
        if (create && this.session == null) {
            this.session = SessionManager.createSession();
        }
        return session;
    }

    public HttpRequestBody getRequestBody() {
        return requestBody;
    }

    public String getParameter(String key) {
        return requestParam.getParameter(key);
    }
    
}
