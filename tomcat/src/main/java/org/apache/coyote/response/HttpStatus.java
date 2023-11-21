package org.apache.coyote.response;

public enum HttpStatus {

    OK(200, "OK"),
    FOUND(302,"FOUND"),
    UNAUTHORIZED(401,"UNAUTORIZED"),
    CREATED(201, "CREATED");

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;

    @Override
    public String toString() {
        return code + " " + message;
    }
}
