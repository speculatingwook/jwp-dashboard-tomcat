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

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
        HttpRequestHeader httpRequestHeader = httpRequest.getHttpRequestHeader();
        String requestUri = httpRequestHeader.getPath();

        URL resource = getClass()
                .getClassLoader()
                .getResource("static" + requestUri);

        try {
            File file = new File(resource.getFile());
            String responseBody = new String(Files.readAllBytes(file.toPath()));

            return HttpResponse.builder()
                    .statusCode(HttpStatusCode.OK.getCode())
                    .statusMessage(HttpStatusCode.OK.getMessage())
                    .addHeader("Content-Type", ContentType.findContentTypeFromUri(requestUri).getContentType())
                    .addHeader("Content-Length", String.valueOf(responseBody.getBytes().length))
                    .body(responseBody)
                    .build();

        } catch (NullPointerException e) {

            // todo: 작동 안함
            return HttpResponse.builder()
                    .statusCode(HttpStatusCode.NOT_FOUND.getCode())
                    .statusMessage(HttpStatusCode.NOT_FOUND.getMessage())
                    .addHeader("Location", NOT_FOUND_PAGE_URL)
                    .build();
        }
    }
}
