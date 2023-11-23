package org.apache.coyote.http11.view.viewResolver;

import org.apache.coyote.http11.view.RedirectView;
import org.apache.coyote.http11.view.View;

public class UrlBasedViewResolver implements ViewResolver {

    private final String REDIRECT_URL_PREFIX = "redirect:";

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
            return new RedirectView(redirectUrl);
        }
        return null;
    }
}
