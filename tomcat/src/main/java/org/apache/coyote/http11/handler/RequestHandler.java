package org.apache.coyote.http11.handler;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.coyote.http11.common.HttpMethod;
import org.apache.coyote.http11.httprequest.HttpRequest;
import org.apache.coyote.http11.scanner.MethodScanner;

public class RequestHandler {

	private HttpRequest httpRequest;
	private Map<HttpMethod, Map<String, Method>> methodMapByHttpMethod;

	private RequestHandler(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
		this.methodMapByHttpMethod = MethodScanner.getInstance().getMethodMapByHttpMethod();
	}

	public static RequestHandler of(HttpRequest httpRequest) {
		return new RequestHandler(httpRequest);
	}

	private Method findMethod(HttpRequest httpRequest) {
		Map<String, Method> methodsForHttpMethod = methodMapByHttpMethod.get(httpRequest.getHttpMethod());
		return methodsForHttpMethod.get(httpRequest.getUri());
	}

	private void invokeMethod(Method method, HttpRequest httpRequest) throws Exception {
		Class<?> controllerClass = method.getDeclaringClass();
		Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
		Object invokedMethodResult = method.invoke(controllerInstance, httpRequest);

	}
}
