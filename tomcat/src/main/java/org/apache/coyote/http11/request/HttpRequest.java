package org.apache.coyote.http11.request;

import org.apache.coyote.http11.session.Cookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String path;
    private String httpVersion;
    private Map<String, String> headers;
    private String body;

    public HttpRequest(InputStream inputStream) throws IOException {
        parseRequest(inputStream);
    }

    private void parseRequest(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // Read start line
        String startLine = reader.readLine();
        String[] startLineParts = startLine.split(" ");
        if (startLineParts.length == 3) {
            method = startLineParts[0];
            path = startLineParts[1];
            httpVersion = startLineParts[2];
        }

        // Read headers
        headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(": ");
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }

        // Read body
        StringBuilder bodyBuilder = new StringBuilder();
        while (reader.ready()) {
            bodyBuilder.append((char) reader.read());
        }
        body = bodyBuilder.toString();
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public Cookie getCookie() {
        String cookieHeader = headers.get("Cookie");
        if (cookieHeader != null) {
            Cookie cookie = new Cookie();
            String[] cookieParts = cookieHeader.split("; ");
            for (String part : cookieParts) {
                String[] keyValue = part.split("=");
                if (keyValue.length == 2) {
                    cookie.setValue(keyValue[0], keyValue[1]);
                }
            }
            return cookie;
        }
        return null;
    }
}
