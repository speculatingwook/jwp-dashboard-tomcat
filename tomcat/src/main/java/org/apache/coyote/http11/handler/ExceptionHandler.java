package org.apache.coyote.http11.handler;

import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.exception.base.BaseRuntimeException;
import org.apache.coyote.http11.httpResponse.HttpResponse;

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
