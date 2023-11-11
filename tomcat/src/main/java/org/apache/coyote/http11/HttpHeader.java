package org.apache.coyote.http11;

public enum HttpHeader {
    ContentType("Content-Type"),
    ContentLength("Content-Length"),
    SetCookie("Set-Cookie"),
    Location("Location"),
    Cookie("Cookie"),

    ;

    private final String headerName;

    HttpHeader(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }
}
