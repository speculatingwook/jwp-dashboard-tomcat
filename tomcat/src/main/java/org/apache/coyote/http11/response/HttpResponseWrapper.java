package org.apache.coyote.http11.response;

import java.util.NoSuchElementException;
import org.apache.coyote.http11.Http11Processor;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.Paths;
import org.apache.coyote.http11.login.LoginHandler;
import org.apache.coyote.http11.request.HttpRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.coyote.http11.HttpMethod.GET;

public class HttpResponseWrapper {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private HttpResponseHeader header;
    private HttpResponseBody body;
    private Paths paths;


    public HttpResponseWrapper(HttpRequestWrapper request) {
        try {
            parseResponse(request);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void parseResponse(HttpRequestWrapper request) {
        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        String path= request.getPath();
        ifGet(method, path);
        login(path, request);
        if (header == null || body == null) {
            throw new NoSuchElementException("해당 페이지를 찾을 수 없습니다: " + path);
        }
    }

    private void login(String path, HttpRequestWrapper request) {
        if (path.equals(Paths.LOGIN.getPath())) {
            LoginHandler login = new LoginHandler(request.getQueryData().get("account"), request.getQueryData().get("password"));

        }
    }

    private void ifGet(HttpMethod method, String path) {
        if(method.equals(GET)){
            for (Paths paths : Paths.values()) {
                String convertedPath = pathConvert(path, paths.getContentType());
                if (convertedPath.equals(paths.getPath())) {
                    body = HttpResponseBody.of(paths.createPath());
                    header = new HttpResponseHeader("200 OK")
                            .addContentType(paths.getContentType())
                            .addContentLength(body.getContentLength());
                }
            }
        }
    }

    private String pathConvert(String path, String contentType) {
        if ((!path.contains(".html")) && contentType.equals("text/html")) {
            return path + ".html";
        }
        return path;
    }


    public String getResponse() {
        return String.join("\r\n", header.getHeaders(), "", body.getBodyContext());
    }
}
