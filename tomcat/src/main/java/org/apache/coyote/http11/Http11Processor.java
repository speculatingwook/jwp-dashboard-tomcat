package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.coyote.Processor;
import org.apache.coyote.http11.HttpRequest.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextstep.jwp.exception.UncheckedServletException;

public class Http11Processor implements Runnable, Processor {

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

			log.info("httpRequest.getHttpMethod(): {}", httpRequest.getHttpMethod());
			log.info("httpRequest.getUri(): {}", httpRequest.getUri());
			log.info("httpRequest.getHttpVersion(): {}", httpRequest.getHttpVersion());
			log.info("httpRequest.getRequestHeaders(): {}", httpRequest.getRequestHeaders());
			log.info("httpRequest.getRequestBody(): {}", httpRequest.getRequestBody());

			// HTTP Response
			HttpStatus httpStatus = null;
			String responseBody = null;


			outputStream.flush();
		} catch (IOException | UncheckedServletException e) {
			log.error(e.getMessage(), e);
		}
	}

}
