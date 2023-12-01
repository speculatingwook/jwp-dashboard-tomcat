package org.apache.coyote.request;

import java.util.HashMap;
import java.util.Map;

public class RequestParam {
    private Map<String, String> requestParam;

    public RequestParam() {
        this.requestParam = new HashMap<>();
    }

    private RequestParam(Map<String, String> requestParam) {
        this.requestParam = requestParam;
    }

    public static RequestParam from(HttpRequestLine requestLine, HttpRequestBody body) {
        Map<String, String> params = new HashMap<>();

        params.putAll(requestLine.getQueryStringMap());
        params.putAll(body.getRequestBodyMap());
        return new RequestParam(params);
    }

    public String getParameter(String key) {
        return requestParam.getOrDefault(key, null);
    }
}
