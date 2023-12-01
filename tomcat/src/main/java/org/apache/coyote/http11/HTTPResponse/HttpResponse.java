package org.apache.coyote.http11.HTTPResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private HttpStatusCode statusCode;

    private HttpResponseHeaders responseHeaders;
    private HttpResponseBody responseBody;

    public HttpResponse(HttpStatusCode statusCode, HttpResponseHeaders responseHeaders, HttpResponseBody responseBody) {
        this.statusCode = statusCode;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }

    public HttpResponse(HttpStatusCode statusCode, HttpResponseHeaders responseHeaders) {
        this.statusCode = statusCode;
        this.responseHeaders = responseHeaders;
    }

    public String generateResponse() {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("HTTP/1.1 ").append(statusCode.getValue()).append("\r\n");
        for (Map.Entry<String, String> entry : responseHeaders.getHeaders().entrySet()) {
            responseBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append(" \r\n");
        }
        responseBuilder.append("\r\n");
        if (responseBody != null) {
            responseBuilder.append(responseBody.getBody());
        }
        return responseBuilder.toString();
    }
}
