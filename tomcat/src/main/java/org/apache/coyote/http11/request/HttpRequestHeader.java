package org.apache.coyote.http11.request;

import lombok.Builder;
import lombok.Getter;
import org.apache.coyote.http11.session.Cookie;

import java.util.Map;
import java.util.Optional;

@Getter
public class HttpRequestHeader {
    private final String method;
    private final String path;
    private final String httpVersion;
    private final Map<String, String> headers;

    @Builder
    public HttpRequestHeader(String method, String path, String httpVersion, Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
        this.headers = headers;
    }

    public Optional<Cookie> getCookie() {
        String cookieHeader = headers.get("Cookie");
        if (cookieHeader != null) {
            Cookie cookie = new Cookie();
            String[] cookieParts = cookieHeader.split("; ");
            for (String part : cookieParts) {
                String[] keyValue = part.split("=");
                if (keyValue.length == 2) {
                    cookie.putValue(keyValue[0], keyValue[1]);
                }
            }
            return Optional.of(cookie);
        }
        // todo: 예외 추가하기
        return null;
    }

}
