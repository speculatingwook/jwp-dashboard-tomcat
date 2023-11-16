package org.apache.coyote.http11.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private int statusCode;
    private String statusMessage;
    private Map<String, String> headers;
    private String body;

    public HttpResponse() {
        this.headers = new HashMap<>();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
