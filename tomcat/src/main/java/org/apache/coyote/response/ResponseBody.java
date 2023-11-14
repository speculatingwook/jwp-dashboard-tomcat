package org.apache.coyote.response;

import org.apache.coyote.request.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResponseBody {
    private String body;

    public ResponseBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public static ResponseBody from(HttpRequest httpRequest) throws IOException {
        return new ResponseBody(readFile(httpRequest.getPath()));
    }

    private static String readFile(String uri) throws IOException {
        if(!uri.contains(".")){
            uri += ".html";
        }
        final URL resourceUrl = ClassLoader.getSystemResource("static" + uri);
        final Path path = new File(resourceUrl.getPath()).toPath();
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    public int length(){
        return body.getBytes().length;
    }

    @Override
    public String toString() {
        return body;
    }
}
