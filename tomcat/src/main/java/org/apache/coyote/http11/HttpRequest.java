package org.apache.coyote.http11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private String requestPath;
    private Map<String, String> params;

    HttpRequest (BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();
        parsePath(firstLine);
        parseParameters();
    }

    private void parsePath(String firstLine) {
        String[] requestLineParts = null;
        if (firstLine != null) {
            requestLineParts = firstLine.split(" ");
            if (requestLineParts.length >= 2) {
                requestPath = requestLineParts[1]; // 요청된 URI (/index.html 등
            }
        }
    }


    //path에서 파라미터 파싱
    private void parseParameters() {
        int paramIndex = requestPath.indexOf('?');
        String paramString = "";
        if (paramIndex != -1) {
            paramString = requestPath.substring(paramIndex + 1);
            requestPath = requestPath.substring(0, paramIndex);
        }
        params = new HashMap<>();
        String[] lines = paramString.split("&");
        if (lines.length > 0) {
            for (String line : lines) {
                String[] keyValue = line.split("=");
                if (keyValue.length > 1) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
    }

    public String getRequestPath() {
        return this.requestPath;
    }

    public Map<String, String> getParams() {
        return this.params;
    }
}
