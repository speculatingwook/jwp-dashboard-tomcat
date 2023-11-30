package org.apache.coyote.http11.HTTPRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private String method;
    private String requestPath;
    private Map<String, String> header;
    private Map<String, String> params;
    private Map<String, String> body;
    private Map<String, String> cookies;
    public HttpRequest(BufferedReader reader) throws IOException {
        StringBuilder headerBuilder = new StringBuilder();
        String firstLine = reader.readLine();
        headerBuilder.append(firstLine).append("\r\n");
        parseMethod(firstLine);
        parsePath(firstLine);
        parseParameters();
        parseHeader(reader);
        parseBody(reader);
        parseCookie();
    }

    private void parseMethod(String firstLine) {
        if (firstLine != null) {
            String[] requestLineParts = firstLine.split(" ");
            if (requestLineParts.length >= 1) {
                method = requestLineParts[0]; // 요청된 HTTP 메서드 (GET 또는 POST 등)
            }
        }
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

    private void parseHeader(BufferedReader reader) throws IOException {
        header = new HashMap<>();
        String line = reader.readLine();
        while ((line != null && !line.isBlank())) {
            System.out.println(line);
            String[] split = line.split(":",2);
            if (split.length == 2) {
                header.put(split[0].trim(), split[1].trim());
            }
            line = reader.readLine();
        }
    }
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

    private void parseBody(BufferedReader reader) throws IOException {
        String messageBody = "";
        body = new HashMap<>();
        if (header.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(header.get("Content-Length"));
            char[] body = new char[contentLength];
            reader.read(body, 0, contentLength);
            messageBody = new String(body);
        }

        System.out.println(messageBody);
        String[] split = messageBody.split("&");
        for (String s : split) {
            String[] keyValue = s.split("=");
            if (keyValue.length == 2) {
                body.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
    }

    private void parseCookie() {
        cookies = new HashMap<>();
        if (header.containsKey("Cookie")) {
            String cookieHeader = header.get("Cookie");
            String[] parts = cookieHeader.split(";");
            for (String part : parts) {
                String[] keyValue = part.trim().split("=",2);
                if (keyValue.length == 2) {
                    System.out.println(keyValue[0] + " -> " + keyValue[1]);
                    cookies.put(keyValue[0], keyValue[1]);
                }
            }
        }
    }
    public String getMethod() {
        return this.method;
    }
    public String getRequestPath() {
        return this.requestPath;
    }
    public Map<String, String> getParams() {
        return this.params;
    }
    public Map<String, String> getBody() {
        return this.body;
    }
    public Map<String, String> getCookie() {
        return this.cookies;
    }

}
