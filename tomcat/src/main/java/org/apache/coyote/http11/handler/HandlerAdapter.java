package org.apache.coyote.http11.handler;

import org.apache.coyote.http11.httprequest.HttpRequest;

import java.util.List;

public class HandlerAdapter {

    static List<Handler> handlers
            = List.of(new RootHandler(), new IndexHandler(), new JavaScriptHandler(), new CSSHandler());

    public static Handler getHandler(HttpRequest httpRequest) {
        for (Handler handler : handlers) {
            if (handler.supports(httpRequest)) {
                return handler;
            }
        }
        return null;
    }
}
