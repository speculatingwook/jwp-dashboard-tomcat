package org.apache.coyote.request;

import org.apache.coyote.session.Session;

import javax.naming.directory.NoSuchAttributeException;
import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private HttpRequestBody requestBody;
    private HttpRequestLine httpRequestLine;
    private HttpRequestHeader header;

    private RequestParam requestParam;
    private Session session;


    public static HttpRequest parse(BufferedReader br) throws NoSuchAttributeException, IOException {
        HttpRequestLine requestLine = HttpRequestLine.parse(br);
        HttpRequestHeader header = HttpRequestHeader.parse(br);
        HttpRequestBody body = HttpRequestBody.parseBody(br, header.getHeader("Content-Length"));
        RequestParam params = RequestParam.from(requestLine,body);

        return new HttpRequest(requestLine, header, body, params);
    }

    private HttpRequest(HttpRequestLine httpRequestLine, HttpRequestHeader header, HttpRequestBody requestBody, RequestParam param) {
        this.httpRequestLine = httpRequestLine;
        this.header = header;
        this.requestBody = requestBody;
        this.requestParam = param;
    }

    public String getHttpMethod() {
        return httpRequestLine.getHttpMethod();
    }

    public String getPath() {
        return httpRequestLine.getPath();
    }

    public Session getSession() {
        if (this.session == null) {
            this.session = new Session();
        }
        return session;
    }

    public Session getSession(boolean create) {
        if (create && this.session == null) {
            this.session = new Session();
        }
        return session;
    }

    public HttpRequestBody getRequestBody() {
        return requestBody;
    }

    public String getParameter(String key) {
        return requestParam.getParameter(key);
    }

    public String getCookie() {
        return header.getHeader("Cookie");
    }
}
