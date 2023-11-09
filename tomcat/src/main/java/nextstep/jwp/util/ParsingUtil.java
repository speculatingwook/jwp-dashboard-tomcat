package nextstep.jwp.util;

import nextstep.jwp.request.RequestBody;
import nextstep.jwp.request.RequestHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ParsingUtil {
    public RequestHeader parseRequestHeader(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String requestLine = reader.readLine();

        String[] requestParts = requestLine.split(" ");
        String method = requestParts[0];
        String path = requestParts[1];
        String contentType = "";
        int contentLength = 0;

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Type: ")) {
                contentType = line.substring("Content-Type: ".length());
            } else if (line.startsWith("Content-Length: ")) {
                contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
            }
        }

        return new RequestHeader(method, path, contentType, contentLength);
    }

    public RequestBody parseRequestBody(InputStream inputStream, RequestHeader requestHeader) throws IOException {
        int contentLength = requestHeader.getContentLength();
        if (contentLength > 0) {
            byte[] bodyBytes = new byte[contentLength];
            int bytesRead = inputStream.read(bodyBytes, 0, contentLength);
            if (bytesRead == contentLength) {
                String requestBody = new String(bodyBytes, "UTF-8");
                return new RequestBody(requestBody);
            }
        }
        return new RequestBody("");
    }
}
