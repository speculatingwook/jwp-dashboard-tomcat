package org.apache.coyote.http11;

import org.apache.coyote.http11.enums.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    int statusCode;
    ContentType contentType;
    String responseBody;
    private Map<String, String> headers = new HashMap<>();

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String generateResponse() {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("HTTP/1.1 ").append(statusCode).append(" OK \r\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            responseBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append(" \r\n");
        }
        responseBuilder.append("Content-Type: ").append(contentType.getValue()).append(";charset=utf-8 \r\n");
        if (responseBody != null) {
            responseBuilder.append("Content-Length: ").append(responseBody.getBytes().length).append(" \r\n");
        }
        responseBuilder.append("\r\n");
        if (responseBody != null) {
            responseBuilder.append(responseBody);
        }
        return responseBuilder.toString();
    }
}
