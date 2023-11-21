package org.apache.coyote.http11.scanner;

import static org.apache.coyote.http11.common.HttpMethod.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.coyote.http11.annotation.GetMapping;
import org.apache.coyote.http11.annotation.PostMapping;
import org.apache.coyote.http11.common.HttpMethod;

import lombok.Getter;

@Getter
public class MethodScanner {

	private Map<String, Method> uriToGetMethodMap;
	private Map<String, Method> uriToPostMethodMap;
	private Map<HttpMethod, Map<String, Method>> methodMapByHttpMethod;

	private MethodScanner() {
		this.uriToGetMethodMap = new HashMap<>();
		this.uriToPostMethodMap = new HashMap<>();
		this.methodMapByHttpMethod = new HashMap<>();
	}

	public static MethodScanner getInstance() {
		return LazyHolder.INSTANCE;
	}

	private static class LazyHolder {
		private static final MethodScanner INSTANCE = new MethodScanner();
	}

	private void scanGetMapping(Set<Class<?>> controllerClasses) {
		for (Class<?> controller : controllerClasses) {
			for (Method method : controller.getDeclaredMethods()) {
				if (method.isAnnotationPresent(GetMapping.class)) {
					GetMapping getMapping = method.getAnnotation(GetMapping.class);
					this.uriToGetMethodMap.put(getMapping.value(), method);
				}
			}
		}
	}

	private void scanPostMapping(Set<Class<?>> controllerClasses) {
		for (Class<?> controller : controllerClasses) {
			for (Method method : controller.getDeclaredMethods()) {
				if (method.isAnnotationPresent(PostMapping.class)) {
					PostMapping postMapping = method.getAnnotation(PostMapping.class);
					this.uriToPostMethodMap.put(postMapping.value(), method);
				}
			}
		}
	}

	public void scanMethods(Set<Class<?>> controllerClasses) {
		scanGetMapping(controllerClasses);
		scanPostMapping(controllerClasses);
	}

	public void mapMethodsWithHttpMethod() {
		methodMapByHttpMethod.put(GET, uriToGetMethodMap);
		methodMapByHttpMethod.put(POST, uriToPostMethodMap);
	}

}
