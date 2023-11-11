package nextstep.jwp.exception;

import org.apache.coyote.http11.exception.ErrorCode;
import org.apache.coyote.http11.exception.base.NotFoundException;

public class NotFoundUserException extends NotFoundException {

    public NotFoundUserException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}
