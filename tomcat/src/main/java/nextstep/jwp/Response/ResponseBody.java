package nextstep.jwp.Response;

public class ResponseBody {
    private String fileContent;

    public ResponseBody(String fileContent) {
        this.fileContent = fileContent;
    }

    public int getLength() {
        return fileContent.length();
    }
}
