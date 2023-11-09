package nextstep.jwp.Response;

public class ResponseHeader {
    private final String HTTP_VERSION = "http/1.1";
    private String responseCode;
    private String responseStatus;
    private String contentType;

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    private int contentLength;

    public ResponseHeader() {}

    public ResponseHeader(String responseCode, String responseStatus, String contentType, int contentLength) {
        this.responseCode = responseCode;
        this.responseStatus = responseStatus;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    @Override
    public String toString() {
        return HTTP_VERSION + " " + responseCode + " " + responseStatus + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + contentLength + " \r\n";
    }
}
