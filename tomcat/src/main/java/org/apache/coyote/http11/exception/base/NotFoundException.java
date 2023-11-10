package org.apache.coyote.http11.exception.base;

import org.apache.coyote.http11.exception.ErrorCode;

public class NotFoundException extends BaseRuntimeException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND_RESOURCE);
    }
}
