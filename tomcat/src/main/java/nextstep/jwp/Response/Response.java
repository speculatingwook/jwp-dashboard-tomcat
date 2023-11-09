package org.apache.coyote.http11;

public class Response {
    private final String HTTP_VERSION = "http/1.1";
    private String responseCode;
    private String responseStatus;
    private String contentType;
    private String fileContent;

    public Response() {}
    public Response(String responseCode, String responseStatus, String contentType, String fileContent) {
        this.responseCode = responseCode;
        this.responseStatus = responseStatus;
        this.contentType = contentType;
        this.fileContent = fileContent;
    }

    @Override
    public String toString() {
        return HTTP_VERSION + " " + responseCode + " " + responseStatus + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + fileContent.length() + " \r\n" +
                "\r\n" +
                fileContent;
    }

}
