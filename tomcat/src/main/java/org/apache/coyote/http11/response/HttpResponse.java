package org.apache.coyote.http11.response;

import org.apache.coyote.http11.*;
import org.apache.coyote.http11.controller.Controller;
import org.apache.coyote.http11.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.NoSuchElementException;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private HttpResponseHeader header;
    private HttpResponseBody body;
    private Session session;
    private Cookie cookie;
    private OutputStream outputStream;

    public HttpResponse(OutputStream outputStream) {
        try {
            this.outputStream = outputStream;
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), e);
        }
    }
    private void sessionLogin() {
        if (session.isSessionValid()) {
            body = HttpResponseBody.of(Paths.INDEX.createPath());
            header = new HttpResponseHeader(StatusCode.OK.getStatus())
                    .addLocation(Paths.INDEX.getPath())
                    .addContentType(Paths.LOGIN.getContentType())
                    .addContentLength(body.getContentLength());
        }
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void getResponse() throws IOException {
        String result = String.join("\r\n", header.getHeaders(), "", body.getBodyContext());
        outputStream.write(result.getBytes());
        outputStream.flush();
    }

    public HttpResponse sendRedirect(String redirectUrl) {
        header = new HttpResponseHeader(StatusCode.FOUND.getStatus())
                .addLocation(redirectUrl);
        return this;
    }
    public HttpResponse addBody(HttpResponseBody body) {
        this.body = body;
        return this;
    }
    public void addHeader(HttpResponseHeader header) {
        this.header = header;
    }

}
