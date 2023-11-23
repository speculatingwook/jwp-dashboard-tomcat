package org.apache.coyote.http11.handler;

import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.Utill.FileFinder;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public class ResourceHttpRequestHandler implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpRequest request, HttpResponse response) {
        String requestUri = request.getPath();
        FileFinder fileFinder = new FileFinder();
        try {
            String fileContent = fileFinder.getFileToStringFromPath(requestUri);
            response.setBody(fileContent);
        } catch (Exception e) {
            System.err.println("Not found file. viewName = " + requestUri);
            response.sendError(HttpStatus.NOT_FOUND, "Not Found");
        }
    }
}
