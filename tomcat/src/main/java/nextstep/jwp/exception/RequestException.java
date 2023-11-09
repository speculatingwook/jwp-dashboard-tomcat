package nextstep.jwp.exception;

import org.apache.util.HttpResponseCode;

public class RequestException extends RuntimeException {
    private HttpResponseCode exceptionCode;

    public RequestException(HttpResponseCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
