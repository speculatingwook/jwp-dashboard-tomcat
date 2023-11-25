package org.apache.coyote.http11.response;

import lombok.Getter;
import org.apache.coyote.http11.session.Cookie;

import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpResponse {
    private HttpResponseHeader httpResponseHeader;
    private HttpResponseBody httpResponseBody;

    public HttpResponse() {

    }

    public HttpResponse(HttpResponseHeader httpResponseHeader, HttpResponseBody httpResponseBody) {
        this.httpResponseHeader = httpResponseHeader;
        this.httpResponseBody = httpResponseBody;
    }

    public static HttpResponseBuilder builder() {
        return new HttpResponse().new HttpResponseBuilder();
    }

    @Override
    public String toString() {
        String header = httpResponseHeader.toString();
        String body = httpResponseBody.toString();

        return header + "\r\n" + body;
    }

    public class HttpResponseBuilder {
        private int statusCode;
        private String statusMessage;
        private Map<String, String> headers = new HashMap<>();
        private Cookie cookie = new Cookie();
        private String body;

        public HttpResponseBuilder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public HttpResponseBuilder statusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
            return this;
        }

        public HttpResponseBuilder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public HttpResponseBuilder addCookie(String name, String value) {
            this.cookie.putValue(name, value);
            return this;
        }

        public HttpResponseBuilder body(String body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            HttpResponseHeader httpResponseHeader = HttpResponseHeader.builder()
                    .statusCode(statusCode)
                    .statusMessage(statusMessage)
                    .headers(headers)
                    .cookie(cookie)
                    .build();

            HttpResponseBody httpResponseBody = HttpResponseBody.builder()
                    .body(body)
                    .build();

            return new HttpResponse(httpResponseHeader, httpResponseBody);
        }
    }
}
