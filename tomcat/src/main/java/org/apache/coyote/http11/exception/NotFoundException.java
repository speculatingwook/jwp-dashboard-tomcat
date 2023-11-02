package org.apache.coyote.http11.exception;

public class NotFoundException extends BaseRuntimeException {

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND_RESOURCE);
    }

    public NotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND_RESOURCE);
    }
}
