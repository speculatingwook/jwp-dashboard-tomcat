package nextstep.jwp.Response;

public class ResponseHeader {
    private final String HTTP_VERSION = "http/1.1";
    private String responseCode;
    private String responseStatus;
    private String contentType;
    private int contentLength;
    private String location;

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
    public void setLocation(String location) {
        this.location = location;
    }

    public ResponseHeader() {}

    public ResponseHeader(String responseCode, String responseStatus, String contentType, int contentLength) {
        this.responseCode = responseCode;
        this.responseStatus = responseStatus;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    @Override
    public String toString() {
        String header = HTTP_VERSION + " " + responseCode + " " + responseStatus + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + contentLength + " \r\n";

        if (location != null && !location.isEmpty()) {
            header += "Location: " + location + "\r\n";
        };

        return header;
    }
}
