package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpMethod;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpStatusCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private Map<HttpMethod, Map<String, Controller>> routes;

    public Router() {
        this.routes = new HashMap<>();
    }

    public void addHandler(HttpMethod httpMethod, String uri, Controller handler) {
        Map<String, Controller> endpointHandler = new HashMap<>();
        endpointHandler.put(uri, handler);
        routes.put(httpMethod, endpointHandler);
    }

    public HttpResponse mappingEndpointHandler(HttpRequest httpRequest) throws IOException {
        String requestUri = httpRequest.getHttpRequestHeader().getPath();
        HttpMethod httpMethod = HttpMethod.valueOf(httpRequest.getHttpRequestHeader().getMethod());

        Map<String, Controller> endpointHandlers = routes.get(httpMethod);

        if (endpointHandlers != null) {
            Controller endpointHandler = endpointHandlers.get(requestUri);
            if (endpointHandler != null) {
                // 해당 URL과 메소드에 맞는 컨트롤러 메소드 실행
                return endpointHandler.handleRequest(httpRequest);
            }
        }

        // 등록된 컨트롤러 메소드가 없을 경우 404 Not Found 반환
        return HttpResponse.builder()
                .statusCode(HttpStatusCode.NOT_FOUND.getCode())
                .statusMessage(HttpStatusCode.NOT_FOUND.getMessage())
                .build();
    }
}
