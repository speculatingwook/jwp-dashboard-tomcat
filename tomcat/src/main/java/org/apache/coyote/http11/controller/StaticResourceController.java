package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.request.HttpRequestHeader;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpStatusCode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class StaticResourceController implements Controller {
    private static final String NOT_FOUND_PAGE_URL = "404.html";
    private String requestUri;
    private String responseBody;

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
        HttpRequestHeader httpRequestHeader = httpRequest.getHttpRequestHeader();
        requestUri = httpRequestHeader.getPath();

        try {
            URL resource = getClass()
                    .getClassLoader()
                    .getResource("static" + requestUri);

            File file = new File(resource.getFile());
            responseBody = new String(Files.readAllBytes(file.toPath()));

            return findResourceSuccessHandler();

        } catch (NullPointerException e) {
            return findResourceFailHandler();

        }
    }

    private HttpResponse findResourceSuccessHandler() {
        return HttpResponse.builder()
                .statusCode(HttpStatusCode.OK.getCode())
                .statusMessage(HttpStatusCode.OK.getMessage())
                .addHeader("Content-Type", ContentType.findContentTypeFromUri(requestUri).getContentType())
                .addHeader("Content-Length", String.valueOf(responseBody.getBytes().length))
                .body(responseBody)
                .build();
    }

    private HttpResponse findResourceFailHandler() {
        return HttpResponse.builder()
                .statusCode(HttpStatusCode.FOUND.getCode())
                .statusMessage(HttpStatusCode.FOUND.getMessage())
                .addHeader("Location", NOT_FOUND_PAGE_URL)
                .build();
    }
}
