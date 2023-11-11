package org.apache.coyote.http11.exception;

import org.apache.coyote.http11.HttpStatus;

public enum ErrorCode {

    // Common
    NOT_FOUND_RESOURCE(HttpStatus.NOT_FOUND, "C001", "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 내부에서 에러가 발생하였습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C003", "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C003", "잘못된 HTTP 메서드입니다."),

    // Request
    FAIL_REQUEST_PARSE(HttpStatus.BAD_REQUEST, "R001", "Request를 파싱하는데 실패"),

    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    ;


    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private final HttpStatus status;
    private final String code;
    private final String message;
}
