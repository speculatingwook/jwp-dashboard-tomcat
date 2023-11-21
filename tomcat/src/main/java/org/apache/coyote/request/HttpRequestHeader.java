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

    public String getHeader(String key){
        return header.get(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        header.keySet().forEach(
                (k) -> sb.append("\n"+k + ": " +header.get(k))
        );
        sb.append("\n");
        return sb.toString();
    }
}
