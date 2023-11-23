package org.apache.coyote.http11.httpResponse;

import org.apache.coyote.http11.HttpHeader;
import org.apache.coyote.http11.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private final String protocol = "HTTP";
    private final String version = "1.1";
    private final String CRLF = "\r\n";
    private HttpStatus status;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    private HttpResponse(HttpStatus status, String body) {
        this.status = status;
        this.body = body;
        //headers.put(HttpHeader.ContentType.getHeaderName(), "text/html;charset=utf-8");
        headers.put(HttpHeader.ContentLength.getHeaderName(), String.valueOf(body.getBytes().length));
    }

    public HttpResponse() {
        this.status = HttpStatus.OK;
        this.body = "";
        //headers.put(HttpHeader.ContentType.getHeaderName(), "text/html;charset=utf-8");
        headers.put(HttpHeader.ContentLength.getHeaderName(), String.valueOf(body.getBytes().length));
    }

    public static HttpResponse of(HttpStatus status, String body) {
        return new HttpResponse(status, body);
    }

    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }

    @Override
    public String toString() {
        String statusLine = createStatusLine();
        String header = createHeader();
        return String.join(CRLF, statusLine, header, "", body);
    }

    private String createStatusLine() {
        return String.format("%s/%s %s %s", protocol, version, status.getValue(), status.getReasonPhrase());
    }

    private String createHeader() {
        return headers.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .reduce((a, b) -> a + "\r\n" + b)
                .orElse("");
    }

    public void sendError(HttpStatus status, String msg) {
        this.status = status;
        this.body = msg;
        headers.put(HttpHeader.ContentLength.getHeaderName(), String.valueOf(body.getBytes().length));
    }

    public void setBody(String body) {
        this.body = body;
        headers.put(HttpHeader.ContentLength.getHeaderName(), String.valueOf(body.getBytes().length));
    }

    public void sendRedirect(String substring) {
        this.status = HttpStatus.FOUND;
        headers.put(HttpHeader.Location.getHeaderName(), substring);
    }

    public void addCookie(String key, String value) {
        headers.put(HttpHeader.SetCookie.getHeaderName(), key + "=" + value);
    }
}
