package nextstep.jwp.conrtroller;

import nextstep.jwp.service.IndexService;
import org.apache.coyote.http11.Response;
import org.apache.util.HttpResponseCode;

public class IndexController {
    private String requestMethod;
    private String requestUrl;
    private IndexService indexService = new IndexService();

    public IndexController(String requestMethod, String requestUrl) {
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
    }

    Response generateResponse() {
        switch(requestUrl) {
            case "/index.html":

                return new Response(responseCode, responseStatus, contentType, fileContent);
                break;
            default:
        }
    }

}
