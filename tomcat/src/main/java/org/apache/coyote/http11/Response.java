package org.apache.coyote.http11;

public class Response {
    private final String HTTP_VERSION = "http/1.1";
    private final String responseCode;
    private final String responseStatus;
    private final String contentType;
    private final String fileContent;

    Response(String responseCode, String responseStatus, String contentType, String fileContent) {
        this.responseCode = responseCode;
        this.responseStatus = responseStatus;
        this.contentType = contentType;
        this.fileContent = fileContent;
    }

    public String toString() {
        return HTTP_VERSION + " " + responseCode + " " + responseStatus + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + fileContent.length() + " \r\n" +
                "\r\n" +
                fileContent;
    }

}
