package org.apache.coyote.http11.HTTPRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestPath {

    private String path;

    private Map<String, String> params;

    public HttpRequestPath(String path, Map<String, String> parmas) {
        this.path = path;
        this.params = parmas;
    }
    public static HttpRequestPath parse(String firstLine) {
        String requestLineParts[] = firstLine.split(" ");
        String requestPath;
        Map<String, String> requestParams = new HashMap<>();

        if (requestLineParts.length < 2) {
            throw new IllegalArgumentException();
        }
        requestPath = requestLineParts[1];

        int paramIndex = requestPath.indexOf('?');
        if (paramIndex != -1) {
            String paramString = requestPath.substring(paramIndex + 1);
            requestPath = requestPath.substring(0, paramIndex);
            Arrays.stream(paramString.split("&"))
                    .map(line -> line.split("="))
                    .filter(keyValue -> keyValue.length > 1)
                    .forEach(keyValue -> requestParams.put(keyValue[0], keyValue[1]));
        }
        return new HttpRequestPath(requestPath, requestParams);
    }

    public String getPath() {
        return path;
    }

    public String getParameter(String field) {
        return params.get(field);
    }
}
