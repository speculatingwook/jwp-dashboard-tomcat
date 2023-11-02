package org.apache.coyote.http11.response;

import java.util.NoSuchElementException;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.request.HttpRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.coyote.http11.HttpMethod.GET;

public class HttpResponseWrapper {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private HttpResponseHeader header;
    private HttpResponseBody body;


    public HttpResponseWrapper(HttpRequestWrapper request) {
        try {
            parseResponse(request);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void parseResponse(HttpRequestWrapper request) {
        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        String path = request.getPath();
        if(method.equals(GET)){
            if (path.equals("/index.html")) {
                body = HttpResponseBody.of("static"+path);
                header = new HttpResponseHeader("200 OK")
                        .addContentType("text/html")
                        .addContentLength(body.getContentLength());
            }
            if (path.equals("/css/styles.css")) {
                body = HttpResponseBody.of("static"+path);
                header = new HttpResponseHeader("200 OK")
                        .addContentType("text/css")
                        .addContentLength(body.getContentLength());
            }
        }
        if (header == null || body == null) {
            throw new NoSuchElementException("해당 페이지를 찾을 수 없습니다: " + path);
        }
    }

    public String getResponse() {
        return String.join("\r\n", header.getHeaders(), "", body.getBodyContext());
    }
}
