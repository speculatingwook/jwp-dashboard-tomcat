package org.apache.coyote.request;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader {

    private Map<String, String> header;

    public HttpRequestHeader() {
        header = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }
}
