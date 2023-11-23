package org.apache.coyote.http11.view;

import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.Utill.FileFinder;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

import java.util.Map;

public class InternalResourceView implements View {

    private final String viewPath;

    public InternalResourceView(String viewPath) {
        this.viewPath = viewPath;

    }

    @Override
    public String getContentType() {
        return "text/html;charset=utf-8";
    }

    @Override
    public void render(Map<String, Object> model, HttpRequest request, HttpResponse response) {
        FileFinder fileFinder = new FileFinder();
        try {
            String fileContent = fileFinder.getFileToStringFromPath(viewPath);
            response.setBody(fileContent);
            response.addHeader("Content-Type", getContentType());
        } catch (Exception e) {
            response.sendError(HttpStatus.NOT_FOUND, "Not Found");
        }
    }
}
