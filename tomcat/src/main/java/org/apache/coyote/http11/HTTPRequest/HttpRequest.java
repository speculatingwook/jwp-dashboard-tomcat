package org.apache.coyote.http11.HTTPRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private final HttpMethod httpMethod;
    private final HttpRequestPath requestPath;
    private final HttpRequestHeaders requestHeader;
    private final HttpRequestBody requestBody;
    private Map<String, String> cookies;
    public HttpRequest(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();
        this.httpMethod = parseMethod(firstLine);
        this.requestPath = parsePath(firstLine);
        this.requestHeader = parseHeader(reader);
        this.requestBody = parseBody(reader);

    }

    private HttpMethod parseMethod(String firstLine) {
        if (firstLine == null) {
            throw new IllegalArgumentException();
        }
        String[] requestLineParts = firstLine.split(" ");
        String methodString = requestLineParts[0];
        return HttpMethod.find(methodString);
    }

    private HttpRequestPath parsePath(String firstLine) {
        if (firstLine == null) {
            throw new IllegalArgumentException();
        }
        return HttpRequestPath.parse(firstLine);
    }

    private HttpRequestHeaders parseHeader(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        List<String> lines = new ArrayList<>();
        while ((line != null && !line.isBlank())) {
            lines.add(line);
            line = reader.readLine();
        }
        return HttpRequestHeaders.parse(lines);
    }
    private int findContentLength() {
        try {
            if (requestHeader.getHeader().containsKey("Content-Length")) {
                return Integer.parseInt(requestHeader.getHeader().get("Content-Length"));
            }
        } catch (IllegalArgumentException e) {

        }
        return 0;
    }

    private HttpRequestBody parseBody(BufferedReader reader) throws IOException {

        int contentLength = findContentLength();
        if (contentLength > 0) {
            char[] body = new char[contentLength];
            reader.read(body, 0, contentLength);
            System.out.println(new String(body));
            return HttpRequestBody.parse(new String(body));
        }
        return HttpRequestBody.empty();
    }

    public HttpMethod getMethod() {
        return this.httpMethod;
    }
    public HttpRequestPath getRequestPath() {
        return this.requestPath;
    }

    public HttpRequestBody getRequestBody() {
        return requestBody;
    }

    public HttpRequestHeaders getRequestHeader() {
        return requestHeader;
    }
}
