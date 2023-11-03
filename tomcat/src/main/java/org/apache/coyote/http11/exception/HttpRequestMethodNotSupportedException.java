package org.apache.coyote.http11.exception;

import org.apache.coyote.http11.exception.base.BadRequestException;

public class HttpRequestMethodNotSupportedException extends BadRequestException {

    public HttpRequestMethodNotSupportedException() {
        super(ErrorCode.METHOD_NOT_ALLOWED);
    }
}
