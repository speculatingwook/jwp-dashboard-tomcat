package org.apache.coyote.http11.handler;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.coyote.http11.httprequest.HttpRequest;
import org.apache.coyote.http11.scanner.MethodScanner;

public class RequestHandler {

	private HttpRequest httpRequest;
	private Map<String, Method> uriToGetMethodMap;
	private Map<String, Method> uriToPostMethodMap;

	private RequestHandler(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
		createMethodMap(MethodScanner.getInstance());
	}

	public static RequestHandler of(HttpRequest httpRequest) {
		return new RequestHandler(httpRequest);
	}

	private void createMethodMap(MethodScanner methodScanner) {
		this.uriToGetMethodMap = methodScanner.getUriToGetMethodMap();
		this.uriToPostMethodMap = methodScanner.getUriToPostMethodMap();
	}
}
