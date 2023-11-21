package org.apache.coyote.http11.request;

import org.apache.coyote.http11.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpRequestHeader {
    private Cookie cookie;
    private String host;
    private String connection;
    private Integer contentLength;
    private static final Logger log = LoggerFactory.getLogger(HttpRequestHeader.class);


    public HttpRequestHeader(List<String> lines) {
        this.contentLength = 0;
        for (String line : lines) {
            if (line.startsWith("Host:")) {
                this.host = line.split(" ")[1];
            }
            if (line.startsWith("Connection")) {
                this.connection = line.split(" ")[1];
            }
            if (line.startsWith("Cookie:")) {
                this.cookie = new Cookie(line);
            }
            if (line.startsWith("Content-Length:")) {
                this.contentLength = Integer.parseInt(line.split(" ")[1]);
            }
        }
    }


    public Integer getContentLength() {
        return contentLength;
    }

    public String getSessionId() {
        return cookie.getSessionId();
    }

    public Cookie getCookie() {
        return cookie;
    }

}
