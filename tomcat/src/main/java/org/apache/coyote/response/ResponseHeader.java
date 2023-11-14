package org.apache.coyote.response;

import org.apache.coyote.request.HttpRequest;
import org.apache.coyote.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class ResponseHeader {
    private List<String> header;

    private ResponseHeader() {
        header = new ArrayList<>();
    }

    public static ResponseHeader of(HttpRequest httpRequest, ResponseBody responseBody) {
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.header.add(Constant.HTTP_VERSION + " " + HttpCode.OK.toString() + " ");
        responseHeader.header.add("Content-Type: " + convertContentType(httpRequest));
        responseHeader.header.add("Content-Length: " + convertLength(responseBody));
        return responseHeader;
    }

    private static String convertLength(ResponseBody responseBody) {
        return String.valueOf(responseBody.length());
    }

    private static String convertContentType(HttpRequest httpRequest) {
        return ContentType.from(httpRequest.getPath()) + ";charset=utf-8";
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        header.stream()
                .forEach(
                        h -> {
                            stringBuilder.append(h)
                                    .append("\n");
                        }
                );
        stringBuilder.append("\r\n");
        return stringBuilder.toString();
    }
}