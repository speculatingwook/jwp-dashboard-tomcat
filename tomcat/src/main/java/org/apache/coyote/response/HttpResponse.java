package org.apache.coyote.response;

import org.apache.coyote.request.HttpRequest;

import java.io.IOException;


public class HttpResponse {
    private ResponseHeader responseHeader;
    private ResponseBody responseBody;

    public HttpResponse() {
    }

    private void makeResponse(HttpRequest httpRequest) throws IOException {
        makeResponseBody(httpRequest);
        makeResponseHeader(httpRequest);
    }

    private void makeResponseBody(HttpRequest httpRequest) throws IOException {
        this.responseBody = ResponseBody.from(httpRequest);
    }

    private void makeResponseHeader(HttpRequest httpRequest) {
        this.responseHeader = ResponseHeader.of(httpRequest, responseBody);
    }


    public String getResponse(HttpRequest httpRequest) throws IOException {
        makeResponse(httpRequest);

        StringBuilder sb = new StringBuilder();
        sb.append(responseHeader)
                .append(responseBody);
        return sb.toString();
    }

}
