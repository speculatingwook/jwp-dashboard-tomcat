package org.apache.coyote.http11.request;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpRequest {
    private HttpRequestHeader httpRequestHeader;
    private HttpRequestBody httpRequestBody;

    public HttpRequest(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        httpRequestHeader = parseRequestHeader(reader);
        httpRequestBody = parseRequestBody(reader);
    }

    private HttpRequestHeader parseRequestHeader(BufferedReader reader) throws IOException {
        String method = "";
        String path = "";
        String httpVersion = "";
        Map<String, String> headers;

        // Read start line
        String startLine = reader.readLine();
        String[] startLineParts = startLine.split(" ");
        if (startLineParts.length == 3) {
            method = startLineParts[0];
            path = startLineParts[1];
            httpVersion = startLineParts[2];
        }

        // Read headers
        headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(": ");
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }

        return HttpRequestHeader.builder()
                .method(method)
                .path(path)
                .httpVersion(httpVersion)
                .headers(headers)
                .build();
}

    private HttpRequestBody parseRequestBody(BufferedReader reader) throws IOException {
        // Read body
        StringBuilder bodyBuilder = new StringBuilder();
        while (reader.ready()) {
            bodyBuilder.append((char) reader.read());
        }

        return HttpRequestBody.builder()
                .body(bodyBuilder.toString())
                .build();
    }
}
