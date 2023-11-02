package org.apache.coyote.http11.exception;

import org.apache.coyote.http11.exception.base.BadRequestException;
import org.apache.coyote.http11.exception.ErrorCode;

public class RequestParseException extends BadRequestException {

    public RequestParseException() {
        super(ErrorCode.FAIL_REQUEST_PARSE);
    }
}
