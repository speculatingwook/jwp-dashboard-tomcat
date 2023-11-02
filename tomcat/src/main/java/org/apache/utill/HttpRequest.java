package org.apache.utill;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final HttpMethod method;
    private final String path;
    private final String version;
    private final Map<String, String> headers;
    private final String body;

    private HttpRequest(HttpMethod method, String path, String version, Map<String, String> headers, String body) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest parse(BufferedReader reader) throws IOException {
        String str;

        // 1. Request Line
        str = reader.readLine();
        String[] requestLines = str.split(" ");
        HttpMethod httpMethod = parseMethod(requestLines[0]);
        String path = parsePath(requestLines[1]);
        String version = parseVersion(requestLines[2]);

        // 2. Request Headers
        Map<String, String> headers = new HashMap<>();
        while (!(str = reader.readLine()).isEmpty()) {
            String[] header = str.split(":");
            headers.put(header[0], header[1]);
        }

        // 3. body
        StringBuilder body = new StringBuilder();
        while (reader.ready()) {
            body.append(reader.readLine());
            body.append("\n");
        }

        return new HttpRequest(httpMethod, path, version, headers, body.toString());
    }

    private static String parseVersion(String requestLine) {
        String trim = StringUtils.trim(requestLine);
        String[] split = trim.split("/");
        return split[1];
    }

    private static String parsePath(String requestLine) {
        return StringUtils.trim(requestLine);
    }

    private static HttpMethod parseMethod(String str) {
        if (str.startsWith("GET")) {
            return HttpMethod.GET;
        }
        else if (str.startsWith("POST")) {
            return HttpMethod.POST;
        }
        else if (str.startsWith("PUT")) {
            return HttpMethod.PUT;
        }
        else if (str.startsWith("DELETE")) {
            return HttpMethod.DELETE;
        }
        else if (str.startsWith("PATCH")) {
            return HttpMethod.PATCH;
        }
        else if (str.startsWith("OPTIONS")) {
            return HttpMethod.OPTIONS;
        }
        else if (str.startsWith("HEAD")) {
            return HttpMethod.HEAD;
        }

        throw new IllegalArgumentException("지원하지 않는 메서드입니다.");
    }

    public String getPath() {
        return path;
    }

    public enum HttpMethod {
        GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD
    }

    public HttpMethod getMethod() {
        return method;
    }
}
