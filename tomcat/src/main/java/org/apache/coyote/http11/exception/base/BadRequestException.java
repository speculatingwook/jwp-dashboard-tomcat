package org.apache.coyote.http11.exception.base;

import org.apache.coyote.http11.exception.ErrorCode;

public class BadRequestException extends BaseRuntimeException {

    public BadRequestException() {
        super(ErrorCode.BAD_REQUEST);
    }

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(String message) {
        super(message, ErrorCode.BAD_REQUEST);
    }
}
