package nextstep;

import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.common.ControllerScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

	private static final String BASE_PACKAGE = "org.apache.coyote.http11.controller";

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		log.info("web server start.");

		ControllerScanner controllerScanner = new ControllerScanner();
		controllerScanner.start(BASE_PACKAGE);

		final var tomcat = new Tomcat();
		tomcat.start();
	}
}
