package nextstep.jwp.exception;

import org.apache.coyote.http11.exception.BadRequestException;
import org.apache.coyote.http11.exception.ErrorCode;

public class RequestParseException extends BadRequestException {

    public RequestParseException() {
        super(ErrorCode.FAIL_REQUEST_PARSE);
    }
}
