package org.apache.coyote.response;

import org.apache.coyote.request.HttpRequest;

import java.io.IOException;


public class HttpResponse {
    private ResponseHeader responseHeader;
    private ResponseBody responseBody;
    private String viewPath;
    private HttpStatus httpStatus;
    private ContentType contentType;
    private String cookie;

    public HttpResponse(String viewPath) {
        this.viewPath = viewPath;
    }

    public HttpResponse() {
        this.httpStatus = HttpStatus.OK;
    }

    public HttpResponse makeResponse() throws IOException {
        makeResponseBody();
        makeResponseHeader();
        return this;
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
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

}
