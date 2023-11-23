package org.apache.coyote.http11.view.viewResolver;

import org.apache.coyote.http11.view.View;

public interface ViewResolver {
    View resolveViewName(String viewName);
}
