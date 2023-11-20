package org.apache.coyote.http11.response;

import lombok.Builder;
import lombok.Getter;
import org.apache.coyote.http11.session.Cookie;

import java.util.Map;

@Getter
public class HttpResponseHeader {
    private final String HTTP_VERSION = "HTTP/1.1";
    private int statusCode;
    private String statusMessage;
    private Map<String, String> headers;
    private Cookie cookie;

    @Builder
    public HttpResponseHeader(int statusCode, String statusMessage, Map<String, String> headers, Cookie cookie) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.headers = headers;
        this.cookie = cookie;
    }

    @Override
    public String toString() {
        String response = HTTP_VERSION + " " + statusCode + " " + statusMessage + "\r\n";

        for(Map.Entry<String, String> header : headers.entrySet()) {
            String headerName = header.getKey();
            String headerValue = header.getValue();
            response += headerName + ": " + headerValue + "\r\n";
        }

        // 쿠키
        for(Map.Entry<String, String> cookie : cookie.getValues().entrySet()) {
            String cookieKey = cookie.getKey();
            String cookieValue = cookie.getValue();
            response += "Set-Cookie: " + cookieKey + "=" + cookieValue + "\r\n";
        }

        return response;
    }
}
