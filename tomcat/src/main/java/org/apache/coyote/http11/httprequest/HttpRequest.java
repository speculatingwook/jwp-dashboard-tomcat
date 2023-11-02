package org.apache.coyote.http11.httprequest;

import nextstep.jwp.exception.RequestParseException;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
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

    public static HttpRequest parse(BufferedReader reader) {
        final String requestLineSplitter = " ";
        final String headerSplitter = ": ";
        String str;

        try {
            // 1. Request Line
            str = reader.readLine();
            String[] requestLines = str.split(requestLineSplitter);
            HttpMethod httpMethod = parseMethod(requestLines[0]);
            String path = parsePath(requestLines[1]);
            String version = parseVersion(requestLines[2]);

            // 2. Request Headers
            Map<String, String> headers = new HashMap<>();
            while (!(str = reader.readLine()).isEmpty()) {
                String[] header = str.split(headerSplitter);
                headers.put(header[0], header[1]);
            }

            // 3. body
            StringBuilder body = new StringBuilder();
            while (reader.ready()) {
                body.append(reader.readLine());
                body.append("\n");
            }

            return new HttpRequest(httpMethod, path, version, headers, body.toString());
        } catch (Exception e) {
            throw new RequestParseException();
        }
    }

    private static HttpMethod parseMethod(String str) {
        return HttpMethod.from(str);
    }

    private static String parsePath(String requestLine) {
        return StringUtils.trim(requestLine);
    }

    private static String parseVersion(String requestLine) {
        String trim = StringUtils.trim(requestLine);
        String[] split = trim.split("/");
        return split[1];
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }
}
