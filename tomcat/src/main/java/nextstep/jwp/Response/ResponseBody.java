package nextstep.jwp.Response;

public class ResponseBody {
    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    private String fileContent;

    public ResponseBody() {}

    public ResponseBody(String fileContent) {
        this.fileContent = fileContent;
    }

    public int getLength() {
        return fileContent.length();
    }
}
