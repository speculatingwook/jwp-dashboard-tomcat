package nextstep.jwp.Response;

public class ResponseBody {
    private String fileContent;

    public ResponseBody() {}

    public ResponseBody(String fileContent) {
        this.fileContent = fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public int getLength() {
        return fileContent.length();
    }

    @Override
    public String toString() {
        return fileContent;
    }
}
