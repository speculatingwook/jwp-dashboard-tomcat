package org.apache.coyote.http11.exception;

import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.HttpStatus;

public class ExceptionHandler {
    public static HttpResponse handle(Exception e) {
        if (e instanceof BaseRuntimeException) {
            BaseRuntimeException baseRuntimeException = (BaseRuntimeException) e;
            return HttpResponse.of(baseRuntimeException.getErrorCode().getStatus(),
                    baseRuntimeException.getErrorCode().getMessage());
        }
        return HttpResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
