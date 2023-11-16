package org.apache.coyote.http11.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String startLine;
    private Map<String, String> headers;
    private String body;

    public HttpRequest(InputStream inputStream) throws IOException {
        parseRequest(inputStream);
    }

    private void parseRequest(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // Read start line
        startLine = reader.readLine();

        // Read headers
        headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(": ");
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }

        // Read body
        StringBuilder bodyBuilder = new StringBuilder();
        while (reader.ready()) {
            bodyBuilder.append((char) reader.read());
        }
        body = bodyBuilder.toString();
    }

    public String getStartLine() {
        return startLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
