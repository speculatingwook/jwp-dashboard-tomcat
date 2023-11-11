package org.apache.coyote.http11.httprequest;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.http11.HttpHeader;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.exception.RequestParseException;
import org.apache.coyote.http11.session.Session;
import org.apache.coyote.http11.session.SessionManager;

public class HttpRequest {
    private final HttpMethod method;
    private final String path;
    private final String version;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private final String body;
    private final Cookie cookie;

    private HttpRequest(HttpMethod method, String path, String version, Map<String, String> headers,
                        Map<String, String> queryParams, String body, Cookie cookie) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
        this.body = body;
        this.queryParams = queryParams;
        this.cookie = cookie;
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
            Map<String, String> queryParams = parseParams(requestLines[1]);
            String version = parseVersion(requestLines[2]);

            // 2. Request Headers
            Map<String, String> headers = new HashMap<>();
            while (!(str = reader.readLine()).isEmpty()) {
                String[] header = str.split(headerSplitter);
                headers.put(header[0], header[1]);
            }

            // 3. body
            int contentLength = Integer.parseInt(headers.getOrDefault(HttpHeader.ContentLength.getHeaderName(), "0"));
            final char[] buffer = new char[contentLength];
            reader.read(buffer, 0, contentLength);
            String body = new String(buffer);

            // 4. cookie
            Cookie cookie = Cookie.parse(headers.getOrDefault(HttpHeader.Cookie.getHeaderName(), ""));

            return new HttpRequest(httpMethod, path, version, headers, queryParams, body, cookie);
        } catch (Exception e) {
            throw new RequestParseException();
        }
    }

    private static Map<String, String> parseParams(String requestLine) {
        String trim = StringUtils.trim(requestLine);
        Map<String, String> params = new HashMap<>();
        String[] split = trim.split("\\?");
        if (split.length == 2) {
            String[] paramSplit = split[1].split("&");
            for (String param : paramSplit) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                } else {
                    params.put(keyValue[0], "");
                }
            }
        }
        return params;
    }

    private static HttpMethod parseMethod(String str) {
        return HttpMethod.from(str);
    }

    private static String parsePath(String requestLine) {
        String trim = StringUtils.trim(requestLine);
        String[] split = trim.split("\\?");
        return split[0];
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

    public String getQueryParam(String key) {
        return queryParams.get(key);
    }

    public Map<String, String> bodyToMap() {
        String[] split = body.split("&");
        Map<String, String> params = new HashMap<>();
        for (String param : split) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            } else {
                params.put(keyValue[0], "");
            }
        }
        return params;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public Session getSession(boolean create) {
        String sessionId = cookie.getValue("JSESSIONID");
        Session session = SessionManager.findSession(sessionId);
        if (session == null && create) {
            session = new Session(sessionId);
            SessionManager.add(session);
        }
        return session;
    }

    public void addSessionId(String sessionId) {
        cookie.addCookie("JSESSIONID", sessionId);
    }
}
