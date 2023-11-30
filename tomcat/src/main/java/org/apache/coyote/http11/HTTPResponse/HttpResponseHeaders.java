package org.apache.coyote.http11.HTTPResponse;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseHeaders {
    private Map<String, String> headers;

    public HttpResponseHeaders() {
        this.headers = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void addContentType(ContentType contentType) {
        headers.put("Content-Type", contentType.getValue() + ";charset=utf-8");
    }

    public void addContentLength(ContentType contentType) {
        headers.put("Content-Type", contentType.getValue() + ";charset=utf-8");
    }

    public void addLocation(String location) {
        headers.put("Location",  location);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

}
