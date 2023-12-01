package org.apache.coyote.http11.HTTPRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestBody {
    private Map<String, String> body;

    private HttpRequestBody(Map<String, String> body) {
        this.body = body;
    }
    public static HttpRequestBody parse(String lines) {
        return new HttpRequestBody(Arrays.stream(lines.split("&"))
                .map(s -> s.split("="))
                .filter(keyValue -> keyValue.length == 2)
                .collect(Collectors.toMap(keyValue -> keyValue[0].trim(), keyValue -> keyValue[1].trim())));
    }

    public static HttpRequestBody empty() {
        return new HttpRequestBody(new HashMap<>());
    }

    public Map<String, String> getBody() {
        return body;
    }

}
