package org.apache.coyote.http11.view.viewResolver;

import org.apache.coyote.http11.Utill.FileFinder;
import org.apache.coyote.http11.view.InternalResourceView;
import org.apache.coyote.http11.view.View;

public class InternalResourceViewResolver implements ViewResolver {

    private final String prefix;
    private final String suffix;

    public InternalResourceViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public View resolveViewName(String viewName) {
        FileFinder fileFinder = new FileFinder();
        String path = prefix + viewName + suffix;
        if (fileFinder.isExist(path)) {
            return new InternalResourceView(prefix + viewName + suffix);
        }
        return null;
    }
}
