package org.apache.coyote.http11.request;

import org.apache.coyote.http11.HttpMethod;

public class HttpRequestLine {
    private final HttpMethod method;
    private final String path;
    private final String protocolVersion;
    public HttpRequestLine(String line) {
        String[] requestLine = line.split(" ");
        this.method = HttpMethod.of(requestLine[0]);
        this.path = requestLine[1];
        this.protocolVersion = requestLine[2];
    }

    public HttpMethod getMethod() {
        return method;
    }
    public String getPath() {
        return path;
    }
}
