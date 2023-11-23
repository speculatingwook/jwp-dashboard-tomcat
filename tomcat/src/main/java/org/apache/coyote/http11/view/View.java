package org.apache.coyote.http11.view;

import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

import java.util.Map;

public interface View {

    default String getContentType() {
        return null;
    }

    void render(Map<String, Object> model, HttpRequest request, HttpResponse response);
}
