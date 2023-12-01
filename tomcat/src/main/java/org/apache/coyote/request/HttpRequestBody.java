package org.apache.coyote.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestBody {

    private String requestBody;

    public HttpRequestBody() {
        requestBody = "";
    }

    private HttpRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public static HttpRequestBody parseBody(BufferedReader br, Object length) throws IOException {
        if (length == null) {
            return new HttpRequestBody();
        }

        int contentLength = Integer.parseInt((String) length);
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        String bodyString = URLDecoder.decode(new String(body), "UTF-8");

        return new HttpRequestBody(bodyString);
    }

    @Override
    public String toString() {
        return requestBody.toString();
    }

    public Map<String, String> getRequestBodyMap() {
        if (requestBody.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, String> bodyMap = new HashMap<>();
        String[] bodyStringArray = requestBody.split("\\&");
        Arrays.stream(bodyStringArray)
                .forEach(keyVal -> {
                    String[] keyValSplit = keyVal.split("=");
                    bodyMap.put(keyValSplit[0], keyValSplit[1]);
                });
        return bodyMap;
    }
}
