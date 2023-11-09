package nextstep.jwp.request;

import java.net.MalformedURLException;
import java.net.URL;

public class RequestHeader {
    private final String HOST = "http://localhost:8080";
    private final String HTTP_VERSION = "http/1.1";
    private String method;
    private String path;
    private String contentType;
    private int contentLength;

    public RequestHeader() {}

    public RequestHeader(String method, String path, String contentType, int contentLength) {
        this.method = method;
        this.path = path;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public URL extractUrl() throws MalformedURLException {
        String urlString = HOST + path;
        return new URL(urlString);
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }
}
