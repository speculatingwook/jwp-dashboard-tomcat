package org.apache.coyote.response;

public enum HttpCode {

    OK(200, "OK");

    HttpCode(int code, String message) {
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
