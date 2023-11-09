package nextstep.jwp.Response;

public class Response {
    private ResponseHeader responseHeader;
    private ResponseBody responseBody;

    public Response() {}

    public Response(ResponseHeader responseHeader, ResponseBody responseBody) {
        this.responseHeader = responseHeader;
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return responseHeader.toString() +
                "\r\n" +
                responseBody.toString();
    }

}
