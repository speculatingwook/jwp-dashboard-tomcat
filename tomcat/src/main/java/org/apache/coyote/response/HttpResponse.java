package org.apache.coyote.response;

import org.apache.coyote.session.Cookie;

import java.io.IOException;


public class HttpResponse {
    private ResponseHeader responseHeader;
    private ResponseBody responseBody;
    private String viewPath;
    private HttpStatus httpStatus;
    private ContentType contentType;
    private Cookie cookie;

    public HttpResponse(String viewPath) {
        this.viewPath = viewPath;
    }

    public HttpResponse() {
        this.httpStatus = HttpStatus.OK;
    }

    public void makeResponse() throws IOException {
        makeResponseBody();
        makeResponseHeader();
    }

    private void makeResponseBody() throws IOException {
        this.responseBody = ResponseBody.from(viewPath);
    }

    private void makeResponseHeader() {
        this.responseHeader = ResponseHeader.of(viewPath, responseBody, httpStatus, cookie);
    }

    public String getResponse() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(responseHeader)
                .append(responseBody);
        return sb.toString();
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
    public void addCookie(String key, String val) {
        this.cookie.addCookie(key,val);
    }

    public void sendRedirect(String viewName) throws IOException {
        this.viewPath = viewName;
        this.contentType = ContentType.from(viewName);
        this.makeResponse();
    }
}