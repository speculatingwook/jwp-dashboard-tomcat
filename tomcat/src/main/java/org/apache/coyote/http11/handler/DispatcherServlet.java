package org.apache.coyote.http11.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import nextstep.jwp.controller.CSSController;
import nextstep.jwp.controller.IndexController;
import nextstep.jwp.controller.JavaScriptController;
import nextstep.jwp.controller.LoginController;
import nextstep.jwp.controller.LoginFormController;
import nextstep.jwp.controller.RootController;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;

public class DispatcherServlet {

    private static final DispatcherServlet INSTANCE = new DispatcherServlet();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private final Map<RequestMapping, Object> handlerMappingMap = new HashMap<>();

    private DispatcherServlet() {
        handlerMappingMap.put(new RequestMapping("*.css", HttpMethod.GET), new CSSController());
        handlerMappingMap.put(new RequestMapping("*.js", HttpMethod.GET), new JavaScriptController());
        handlerMappingMap.put(new RequestMapping("/index.html", HttpMethod.GET), new IndexController());
        handlerMappingMap.put(new RequestMapping("/", HttpMethod.GET), new RootController());
        handlerMappingMap.put(new RequestMapping("/login", HttpMethod.GET), new LoginFormController());
        handlerMappingMap.put(new RequestMapping("/login", HttpMethod.POST), new LoginController());

        handlerAdapters.add(new ControllerHandlerAdapter());
    }

    public static DispatcherServlet getInstance() {
        return INSTANCE;
    }

    public HttpResponse service(HttpRequest request, HttpResponse response) {
        Object handler = getHandler(request);
        if (handler == null) {
            System.err.println("Not found handler");
            response.sendError(HttpStatus.NOT_FOUND, "Not Found");
            return response;
        }

        HandlerAdapter adapter = getHandlerAdapter(handler);
        return adapter.handle(request, response, handler);
    }

    private Object getHandler(HttpRequest request) {
        HttpMethod method = request.getMethod();
        String path = request.getPath();
        return handlerMappingMap.get(new RequestMapping(path, method));
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    public static class RequestMapping {
        private final String path;
        private final HttpMethod method;

        public RequestMapping(String path, HttpMethod method) {
            this.path = path;
            this.method = method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RequestMapping that = (RequestMapping) o;

            // TODO 정규표현식 처리
            if (Objects.equals(this.path, "*.css")) {
                return that.path.endsWith(".css");
            }
            if (Objects.equals(this.path, "*.js")) {
                return that.path.endsWith(".js");
            }
            if (Objects.equals(that.path, "*.css")) {
                return this.path.endsWith(".css");
            }
            if (Objects.equals(that.path, "*.js")) {
                return this.path.endsWith(".js");
            }

            return Objects.equals(path, that.path) && Objects.equals(method, that.method);
        }

        @Override
        public int hashCode() {
            if (this.path.endsWith(".css")) {
                return Objects.hash(".css", method);
            }
            if (this.path.endsWith(".js")) {
                return Objects.hash(".js", method);
            }
            return Objects.hash(path, method);
        }
    }
}
