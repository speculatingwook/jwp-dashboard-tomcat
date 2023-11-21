package org.apache.coyote.http11.response;

import org.apache.coyote.http11.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Objects;

public class HttpResponseBody {

    private final String bodyContext;
    private final int contentLength;
    private static final Logger log = LoggerFactory.getLogger(HttpResponseBody.class);

    public HttpResponseBody(final String bodyContext) {
        this.bodyContext = bodyContext;
        this.contentLength = bodyContext.getBytes().length;
    }

    public static HttpResponseBody of(final String fileName) {
        return new HttpResponseBody(bodyOf(fileName));
    }

    private static String bodyOf(final String fileName) {
        try {
            URL url = HttpResponseBody.class.getClassLoader().getResource(fileName);
            String file = Objects.requireNonNull(url).getFile();
            Path path = new File(file).toPath();
            return String.join("\r\n", new String(Files.readAllBytes(path)));
        } catch (NullPointerException | IOException e) {
            throw new NoSuchElementException("해당 이름의 파일을 찾을 수 없습니다: " + fileName);
        }
    }

    public String getBodyContext() {
        return bodyContext;
    }

    public int getContentLength() {
        return contentLength;
    }
}
