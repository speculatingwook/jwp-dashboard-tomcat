package nextstep;

import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.scanner.ControllerScanner;
import org.apache.coyote.http11.scanner.MethodScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

	private static final String BASE_PACKAGE = "org.apache.coyote.http11.controller";

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		log.info("web server start.");
		ControllerScanner controllerScanner = new ControllerScanner(BASE_PACKAGE);
		MethodScanner methodScanner = MethodScanner.getInstance();
		methodScanner.scan(controllerScanner.getControllerClasses());
		log.info("uriToMethodMap: {}", methodScanner.getUriToMethodMap());
		final var tomcat = new Tomcat();
		tomcat.start();
	}
}
