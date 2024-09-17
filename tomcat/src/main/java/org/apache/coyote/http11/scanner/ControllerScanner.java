package org.apache.coyote.http11.scanner;

import java.util.Set;

import org.apache.coyote.http11.annotation.Controller;
import org.reflections.Reflections;

public class ControllerScanner {

	private final Reflections reflections;

	public ControllerScanner(String basePackage) {
		this.reflections = new Reflections(basePackage);
	}

	public Set<Class<?>> getControllerClasses() {
		return reflections.getTypesAnnotatedWith(Controller.class);
	}
}
