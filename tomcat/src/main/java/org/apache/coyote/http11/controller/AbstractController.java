package org.apache.coyote.http11.controller;

import jakarta.servlet.http.Cookie;
import org.apache.coyote.http11.HTTPRequest.HttpRequest;
import org.apache.coyote.http11.HTTPResponse.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractController implements Controller {
    private static final Map<String, ContentType> CONTENT_TYPES = new HashMap<>();
    private static final HttpResponseHeaders headers = new HttpResponseHeaders();

    static {
        // 일반적인 파일 확장자에 대한 MIME 타입 매핑
        CONTENT_TYPES.put("html", ContentType.HTML);
        CONTENT_TYPES.put("css", ContentType.CSS);
        CONTENT_TYPES.put("js", ContentType.JS);
    }
    public HttpResponse getStaticResourceFile(String path) throws IOException {
        if (path.equals("/")) {
            return generateHTTPResponseWithBody(HttpStatusCode.NOT_FOUND, ContentType.HTML, "Resource Not Found");
        }
        File resourceFile = new File("/Users/hayoon/spring/jwp-dashboard-http-mission/tomcat/src/main/resources/static" + path);
        if (resourceFile.exists()) {
            byte[] content = Files.readAllBytes(Path.of(resourceFile.getAbsolutePath()));
            String responseBody = new String(content);
            String fileExtension = getFileExtension(path);
            return generateHTTPResponseWithBody(HttpStatusCode.OK, CONTENT_TYPES.get(fileExtension), responseBody);
        } else {
            return generateHTTPResponseWithBody(HttpStatusCode.NOT_FOUND, ContentType.HTML, "Resource Not Found");
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    public void generateRedirection(String location) {
        headers.addLocation(location);
    }

    public void addCookie(Cookie cookie) {
        headers.addHeader("Set-Cookie", "JSESSIONID=" + cookie.getValue());
    }

    public HttpResponse generateHTTPResponse(ContentType contentType, HttpStatusCode statusCode) {
        headers.addContentType(contentType);
        HttpResponse httpResponse = new HttpResponse(statusCode, headers);
        return httpResponse;
    }

    public HttpResponse generateHTTPResponseWithBody(HttpStatusCode statusCode, ContentType contentType, String body) {
        headers.addContentType(contentType);
        HttpResponseBody responseBody = new HttpResponseBody(body);
        HttpResponse httpResponse = new HttpResponse(statusCode, headers, responseBody);
        return httpResponse;
    }

}
