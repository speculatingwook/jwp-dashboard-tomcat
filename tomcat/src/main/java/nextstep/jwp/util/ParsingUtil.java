package nextstep.jwp.util;

import nextstep.jwp.request.Request;
import nextstep.jwp.request.RequestBody;
import nextstep.jwp.request.RequestHeader;

import java.io.*;

public class ParsingUtil {
    public Request parseRequest(InputStream inputStream) throws IOException {
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

        RequestHeader requestHeader = new RequestHeader(method, path, contentType, contentLength);

        StringBuilder bodyBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            bodyBuilder.append(line).append("\r\n");
        }

        RequestBody requestBody = new RequestBody(bodyBuilder.toString());

        return new Request(requestHeader, requestBody);
    }
}
