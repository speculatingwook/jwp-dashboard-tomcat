package org.apache.coyote.http11.exception.base;

import org.apache.coyote.http11.exception.ErrorCode;

public class InternalServerException extends BaseRuntimeException {

    public InternalServerException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public InternalServerException(String message) {
        super(message, ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
