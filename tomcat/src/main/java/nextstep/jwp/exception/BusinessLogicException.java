package nextstep.jwp.exception;

import org.apache.util.HttpResponseCode;

public class BusinessLogicException extends RuntimeException {
    private HttpResponseCode exceptionCode;

    public BusinessLogicException(HttpResponseCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
