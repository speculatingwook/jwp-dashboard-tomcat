package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.coyote.Processor;
import org.apache.coyote.http11.HttpRequest.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextstep.jwp.exception.UncheckedServletException;

public class Http11Processor implements Runnable, Processor {

	private static final int HTTP_METHOD = 0;
	private static final int HTTP_URI = 1;
	private static final int HTTP_VERSION = 2;
	private static final int HEADER_KEY = 0;
	private static final int HEADER_VALUE = 1;
	private static final String BLANK_SPACE = " ";
	private static final String HEADER_SPLITTER = ": ";

	private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

	private final Socket connection;

	public Http11Processor(final Socket connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
		process(connection);
	}

	@Override
	public void process(final Socket connection) {
		try (final var inputStream = connection.getInputStream();
			 final var outputStream = connection.getOutputStream()) {

			// HTTP Request
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			HttpRequest httpRequest = HttpRequest.from(bufferedReader);
			log.info("httpRequest: {}", httpRequest);

			// HTTP Response
			HttpStatus httpStatus = null;
			String responseBody = null;

			if (httpMethod == null) {
				responseBody = "Unsupported Method";
				httpStatus = HttpStatus.BAD_REQUEST;
			} else {
				if (httpMethod.equals(HttpMethod.GET)) {
					String resourceContent = findResources(uri);
					log.info("resourceContent: {}", resourceContent);
					if (resourceContent != null) {
						responseBody = resourceContent;
						httpStatus = HttpStatus.OK;
					} else {
						responseBody = "Resource Not Found";
						httpStatus = HttpStatus.NOT_FOUND;
					}
				}
			}

			// creating HTTP Response
			String response = String.join("\r\n",
				"HTTP/1.1 " + httpStatus.getCode() + " " + httpStatus.getStatus(),
				"Content-Type: " + ContentType.findByExtension(uri.split("\\.")[1]).getMimeType(),
				"Content-Length: " + responseBody.getBytes().length,
				"",
				responseBody);

			outputStream.write(response.getBytes());
			outputStream.flush();
		} catch (IOException | UncheckedServletException e) {
			log.error(e.getMessage(), e);
		}
	}

	private String findResources(String uri) {
		try {
			Path filePath = Paths.get(System.getProperty("user.dir"), "tomcat/src/main/resources/static" + uri);
			log.info("filePath: {}", filePath);
			if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
				return Files.readString(filePath);
			}
		} catch (IOException e) {
			log.error("Error reading resource: " + uri, e);
		}
		return null;
	}
}
