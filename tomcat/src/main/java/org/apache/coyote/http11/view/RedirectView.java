package org.apache.coyote.http11.view;

import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

import java.util.Map;

public class RedirectView implements View {

    private final String redirectUrl;

    public RedirectView(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void render(Map<String, Object> model, HttpRequest request, HttpResponse response) {
        response.sendRedirect(redirectUrl);
    }
}
