package org.apache.coyote.http11.HTTPResponse;

public enum HttpStatusCode {

    OK(200),
    CREATED(201),
    FOUND(302),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    ;
    private final int code;

    HttpStatusCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return code + " " + name();
    }

}
