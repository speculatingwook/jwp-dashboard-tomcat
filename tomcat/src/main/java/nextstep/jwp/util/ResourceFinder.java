package nextstep.jwp.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class ResourceFinder {
    public String getResource(String url) throws IOException {
        URL resource = getClass().getClassLoader().getResource("static" + url);
        return new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
    }

    public String getFileExtension(String url) {
        int lastDotIndex = url.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return url.substring(lastDotIndex + 1);
        }
        return "";
    }

    public String getContentType(String fileExtension) {
        String contentType = ContentType.valueOf(fileExtension.toUpperCase()).getContentType();
        if(contentType != null) return contentType;
        return "application/octet-stream";
    }
}
