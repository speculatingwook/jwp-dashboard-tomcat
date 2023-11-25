package org.apache.coyote.http11.request;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.http11.Cookie;
import org.apache.coyote.http11.HttpSession;
import org.apache.coyote.http11.HttpSessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HttpRequest {
    private final HttpRequestLine httpRequestLine;
    private final HttpRequestHeader httpRequestHeader;
    private final HttpRequestBody httpRequestBody;
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private static final String EMPTY_REQUEST = "요청이 비어있습니다.";
    public HttpRequest(InputStream inputStream) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> lines = bufferReaderToLines(reader);
        if (lines == null || lines.isEmpty()) {
            throw new NoSuchElementException(EMPTY_REQUEST);
        }
        this.httpRequestLine = new HttpRequestLine(lines.get(0));
        this.httpRequestHeader = new HttpRequestHeader(lines);
        this.httpRequestBody = new HttpRequestBody(readBody(reader, httpRequestHeader.getContentLength()));
    }

    private List<String> bufferReaderToLines(final BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            lines.add(line);
        }
        return lines;
    }

    private String readBody(BufferedReader reader, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        reader.read(buffer, 0, contentLength);
        return new String(buffer);
    }

    public HttpRequestLine getHttpRequestLine() {
        return httpRequestLine;
    }

    public HttpRequestHeader getHttpRequestHeader() {
        return httpRequestHeader;
    }

    public HttpRequestBody getHttpRequestBody() {
        return httpRequestBody;
    }

    public Cookie getCookie() {
        return httpRequestHeader.getCookie();
    }

    public HttpSession getSession() {
        return HttpSessions.getSession(getCookie().getSessionId());
    }

}
