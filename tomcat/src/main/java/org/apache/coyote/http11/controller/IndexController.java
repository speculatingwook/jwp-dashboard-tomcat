package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpStatusCode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;

public class IndexController implements Controller {
    private final ArrayList<String> urls;

    public IndexController() {
        urls = new ArrayList<>();
        urls.add("/index.html");
    }

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String requestUri = httpRequest.getPath();

        URL resource = getClass()
                .getClassLoader()
                .getResource("static" + requestUri);
        File file = new File(resource.getFile());

        String responseBody = new String(Files.readAllBytes(file.toPath()));

        return httpResponse = new HttpResponse()
                .statusCode(HttpStatusCode.OK.getCode())
                .statusMessage(HttpStatusCode.OK.getMessage())
                .addHeader("Content-Type", ContentType.findContentTypeFromUri(requestUri).getContentType())
                .addHeader("Content-Length", String.valueOf(responseBody.getBytes().length))
                .body(responseBody)
                .build();
    }
}
