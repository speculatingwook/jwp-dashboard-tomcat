package nextstep.jwp.controller;

import nextstep.jwp.Response.ResponseBody;
import nextstep.jwp.Response.ResponseHeader;
import nextstep.jwp.request.Request;
import nextstep.jwp.service.IndexService;
import nextstep.jwp.Response.Response;
import nextstep.jwp.util.ResourceFinder;
import org.apache.util.HttpResponseCode;

import java.io.IOException;

public class IndexController {
    private IndexService indexService = new IndexService();

    // url: /index
    public Response handleRequest(Request request) throws IOException {
        ResourceFinder resourceFinder = new ResourceFinder();
        ResponseBody responseBody = new ResponseBody(resourceFinder.getResource("/index.html"));
        ResponseHeader responseHeader = new ResponseHeader(HttpResponseCode.OK.toString(),
                HttpResponseCode.OK.getReasonPhrase(),
                resourceFinder.getContentType(resourceFinder.getFileExtension("/index.html")),
                responseBody.getLength());
        return new Response(responseHeader, responseBody);
    }

}
