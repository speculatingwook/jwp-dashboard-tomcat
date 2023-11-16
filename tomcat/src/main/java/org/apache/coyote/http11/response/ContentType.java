package org.apache.coyote.http11.response;

import java.util.Arrays;

public enum ContentType {
    HTML("html", "text/html;charset=utf-8"),
    JS("js", "application/javascript"),
    CSS("css", "text/css"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    PNG("png", "image/png"),
    GIF("gif", "image/gif");

    private final String extension;
    private final String contentType;

    ContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    // 확장자가 uri에 포함되지 않은 요청일 경우 우선 html인 것으로 가정
    public static ContentType findContentTypeFromUri(String requestUri) {
        return Arrays.stream(ContentType.values())
                .filter(contentType -> requestUri.endsWith(contentType.extension.toLowerCase()))
                .findFirst()
                .orElse(HTML);
    }
}