package org.apache.coyote.http11.handler;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.coyote.http11.httprequest.HttpRequest;
import org.apache.coyote.http11.httpresponse.HttpResponse;
import org.apache.coyote.http11.scanner.MethodScanner;

public class RequestHandler {

	private HttpRequest httpRequest;
	private HttpResponse httpResponse;
	private Map<String, Method> uriToGetMethodMap;
	private Map<String, Method> uriToPostMethodMap;

	private RequestHandler(HttpRequest httpRequest, HttpResponse httpResponse) {
		this.httpRequest = httpRequest;
		this.httpResponse = httpResponse;
		MethodScanner methodScanner = MethodScanner.getInstance();
		this.uriToGetMethodMap = methodScanner.getUriToGetMethodMap();
		this.uriToPostMethodMap = methodScanner.getUriToPostMethodMap();
	}

	public static RequestHandler of(HttpRequest httpRequest, HttpResponse httpResponse) {
		return new RequestHandler(httpRequest, httpResponse);
	}

}
