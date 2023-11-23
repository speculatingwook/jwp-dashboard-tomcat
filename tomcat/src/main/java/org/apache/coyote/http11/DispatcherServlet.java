package org.apache.coyote.http11;

import nextstep.jwp.controller.*;
import org.apache.coyote.http11.adapter.ControllerHandlerAdapter;
import org.apache.coyote.http11.adapter.HandlerAdapter;
import org.apache.coyote.http11.adapter.HttpRequestHandlerAdapter;
import org.apache.coyote.http11.handler.ResourceHttpRequestHandler;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.httprequest.HttpRequest;
import org.apache.coyote.http11.view.ModelAndView;
import org.apache.coyote.http11.view.View;
import org.apache.coyote.http11.view.viewResolver.InternalResourceViewResolver;
import org.apache.coyote.http11.view.viewResolver.UrlBasedViewResolver;
import org.apache.coyote.http11.view.viewResolver.ViewResolver;

import java.util.*;

public class DispatcherServlet {

    private static final DispatcherServlet INSTANCE = new DispatcherServlet();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private final List<ViewResolver> viewResolvers = new ArrayList<>();
    private final Map<RequestMapping, Object> handlerMappingMap = new HashMap<>();
    private final String sessionKey = "JSESSIONID";

    private DispatcherServlet() {
        ResourceHttpRequestHandler resourceHttpRequestHandler = new ResourceHttpRequestHandler();
        handlerMappingMap.put(new RequestMapping("*.css", HttpMethod.GET), resourceHttpRequestHandler);
        handlerMappingMap.put(new RequestMapping("*.js", HttpMethod.GET), resourceHttpRequestHandler);
        handlerMappingMap.put(new RequestMapping("/401.html", HttpMethod.GET), resourceHttpRequestHandler);
        handlerMappingMap.put(new RequestMapping("/404.html", HttpMethod.GET), resourceHttpRequestHandler);
        handlerMappingMap.put(new RequestMapping("/500.html", HttpMethod.GET), resourceHttpRequestHandler);
        handlerMappingMap.put(new RequestMapping("/index.html", HttpMethod.GET), resourceHttpRequestHandler);

        handlerMappingMap.put(new RequestMapping("/", HttpMethod.GET), new RootController());
        handlerMappingMap.put(new RequestMapping("/login", HttpMethod.GET), new LoginFormController());
        handlerMappingMap.put(new RequestMapping("/login", HttpMethod.POST), new LoginController());
        handlerMappingMap.put(new RequestMapping("/register", HttpMethod.GET), new RegisterFormController());
        handlerMappingMap.put(new RequestMapping("/register", HttpMethod.POST), new RegisterController());

        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new HttpRequestHandlerAdapter());

        viewResolvers.add(new InternalResourceViewResolver("/", ".html"));
        viewResolvers.add(new UrlBasedViewResolver());
    }

    public static DispatcherServlet getInstance() {
        return INSTANCE;
    }


    public void doService(HttpRequest request, HttpResponse response) {
        try {
            if (request.getCookie().getValue(sessionKey) == null) {
                String sessionId = UUID.randomUUID().toString();
                request.addSessionId(sessionId);
                response.addCookie(sessionKey, sessionId);
            }

            doDispatch(request, response);
        } catch (Exception e) {
            System.err.println("Exception : " + e.getMessage());
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    private void doDispatch(HttpRequest request, HttpResponse response) {
        Object handler = getHandler(request);
        if (handler == null) {
            System.err.println("Not found controller");
            response.sendError(HttpStatus.NOT_FOUND, "Not Found");
            return;
        }

        HandlerAdapter adapter = getHandlerAdapter(handler);
        ModelAndView mv = adapter.handle(request, response, handler);
        if (mv != null) {
            render(mv, request, response);
        }
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

    private void render(ModelAndView mv, HttpRequest request, HttpResponse response) {
        String viewName = mv.getViewName();
        View view = resolveViewName(viewName);
        if (view == null) {
            System.err.println("Could not resolve view with name '" + viewName + "'");
            response.sendError(HttpStatus.NOT_FOUND, "Not Found");
            return;
        }
        view.render(mv.getModel(), request, response);
    }

    private View resolveViewName(String viewName) {
        for (ViewResolver viewResolver : this.viewResolvers) {
            View view = viewResolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }
        return null;
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
