package org.apache.coyote.http11.handler;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.coyote.http11.httprequest.HttpRequest;
import org.apache.coyote.http11.httpresponse.HttpResponse;
import org.apache.coyote.http11.scanner.MethodScanner;

public class RequestHandler {

	private HttpRequest httpRequest;
	private HttpResponse httpResponse;
	private Map<String, Method> uriToMethodMap;

	private RequestHandler(HttpRequest httpRequest, HttpResponse httpResponse) {
		this.httpRequest = httpRequest;
		this.httpResponse = httpResponse;
		this.uriToMethodMap = MethodScanner.getInstance().getUriToMethodMap();
	}

	public static RequestHandler of(HttpRequest httpRequest, HttpResponse httpResponse) {
		return new RequestHandler(httpRequest, httpResponse);
	}


}
