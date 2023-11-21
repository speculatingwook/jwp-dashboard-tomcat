package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.Paths;
import org.apache.coyote.http11.StatusCode;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpResponseBody;
import org.apache.coyote.http11.response.HttpResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticContentController extends AbstractController{
    private HttpResponseHeader header;
    private HttpResponseBody body;
    private static final Logger log = LoggerFactory.getLogger(StaticContentController.class);
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        String path = request.getHttpRequestLine().getPath();
        String convertedPath = pathConvert(path);
        for (Paths paths : Paths.values()) {
            if (paths.getPath().equals(convertedPath)) {
                to(convertedPath);
                response.addBody(body).addHeader(header);
            }
        }
    }
    public void to(String path) {
        for (Paths paths : Paths.values()) {
            if (path.equals(paths.getPath())) {
                body = HttpResponseBody.of(paths.createPath());
                header = new HttpResponseHeader(StatusCode.OK.getStatus())
                        .addContentType(paths.getContentType())
                        .addContentLength(body.getContentLength());
            }
        }
    }
    private static String pathConvert(String path) {
        for (Paths paths : Paths.values()) {
            if (paths.getPath().contains(path)) {
                if ((!path.contains(".html")) && paths.getContentType().equals("text/html")) {
                    return path + ".html";
                }
            }
        }
        return path;
    }
}
