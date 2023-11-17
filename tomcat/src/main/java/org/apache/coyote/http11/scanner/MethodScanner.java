package org.apache.coyote.http11.scanner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.coyote.http11.annotation.RequestMapping;

public class MethodScanner {

	private Map<String, Method> uriToMethodMap;

	private MethodScanner() {
		this.uriToMethodMap = new HashMap<>();
	}

	public static MethodScanner getInstance() {
		return LazyHolder.INSTANCE;
	}

	private static class LazyHolder {
		private static final MethodScanner INSTANCE = new MethodScanner();
	}

	public void scan(Set<Class<?>> controllerClasses) {
		for (Class<?> controller : controllerClasses) {
			for (Method method : controller.getDeclaredMethods()) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
					this.uriToMethodMap.put(requestMapping.value(), method);
				}
			}
		}
	}

	public Map<String, Method> getUriToMethodMap() {
		return uriToMethodMap;
	}

}
