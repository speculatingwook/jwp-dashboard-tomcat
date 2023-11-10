package org.apache.coyote.http11.request;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestBody {
    private final Map<String, String> parameters;

    public HttpRequestBody(String line) {
        this.parameters = parseBody(line);
    }
    private Map<String, String> parseBody(String line) {
        Map<String, String> parameters = new HashMap<>();
        String[] keyValuePairs = line.split("&");
        for (String keyValue : keyValuePairs) {
            String parts[] = keyValue.split("=");
            if (parts.length == 2) {
                parameters.put(parts[0], parts[1]);
            }
        }
        return parameters;
    }

    public String getValue(String key) {
        if (parameters.containsKey(key)) {
            return parameters.get(key);
        }
        return "key invalid";
    }
}
