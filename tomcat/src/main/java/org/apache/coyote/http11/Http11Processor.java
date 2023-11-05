package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextstep.jwp.exception.UncheckedServletException;

public class Http11Processor implements Runnable, Processor {

	private static final int HTTP_METHOD = 0;
	private static final int HTTP_URI = 1;
	private static final int HTTP_VERSION = 2;
	private static final int HEADER_KEY = 0;
	private static final int HEADER_VALUE = 0;
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

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			String line = bufferedReader.readLine();
			String[] splittedRequestLine = line.split(BLANK_SPACE);

			String httpMethod = splittedRequestLine[HTTP_METHOD];
			String uri = splittedRequestLine[HTTP_URI];
			String httpVersion = splittedRequestLine[HTTP_VERSION];

			final var responseBody = "Hello world!";

			final var response = String.join("\r\n",
				"HTTP/1.1 200 OK ",
				"Content-Type: text/html;charset=utf-8 ",
				"Content-Length: " + responseBody.getBytes().length + " ",
				"",
				responseBody);

			outputStream.write(response.getBytes());
			outputStream.flush();
		} catch (IOException | UncheckedServletException e) {
			log.error(e.getMessage(), e);
		}
	}
}
