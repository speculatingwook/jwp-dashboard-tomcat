package org.apache.coyote.response;

import org.apache.coyote.request.HttpRequest;

import java.io.IOException;


public class HttpResponse {
    private ResponseHeader responseHeader;
    private ResponseBody responseBody;
    private String viewPath;
    private HttpStatus httpStatus;
    private ContentType contentType;



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
        this.responseHeader = ResponseHeader.of(viewPath, responseBody, httpStatus);
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

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
