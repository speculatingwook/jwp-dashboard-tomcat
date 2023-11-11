package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.HttpRequest;
import org.apache.coyote.http11.HttpResponse;
import org.apache.coyote.http11.enums.ContentType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StaticController implements Controller {

    private static final Map<String, ContentType> CONTENT_TYPES = new HashMap<>();

    static {
        // 일반적인 파일 확장자에 대한 MIME 타입 매핑
        CONTENT_TYPES.put("html", ContentType.HTML);
        CONTENT_TYPES.put("css", ContentType.CSS);
        CONTENT_TYPES.put("js", ContentType.JS);
    }

    @Override
    public void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        getStaticResourceFile(httpRequest.getRequestPath(), httpResponse);
    }
    public void getStaticResourceFile(String path, HttpResponse httpResponse) throws IOException {
        File resourceFile = new File("/Users/hayoon/spring/jwp-dashboard-http-mission/tomcat/src/main/resources/static" + path);
        if (resourceFile.exists()) {
            byte[] content = Files.readAllBytes(Path.of(resourceFile.getAbsolutePath()));
            String responseBody = new String(content);
            String fileExtension = getFileExtension(path);
            httpResponse.setResponseBody(responseBody);
            httpResponse.setStatusCode(200); // HTTP 상태코드 200 (OK)
            httpResponse.setContentType(CONTENT_TYPES.get(fileExtension));
        } else {
            String responseBody = "Resource not found.";
            httpResponse.setResponseBody(responseBody);
            httpResponse.setStatusCode(404); // HTTP 상태코드 404 (Not Found)
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
}
