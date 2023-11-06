package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.coyote.Processor;
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

			String line = bufferedReader.readLine();
			String[] splittedRequestLine = line.split(BLANK_SPACE);

			String httpMethod = splittedRequestLine[HTTP_METHOD];
			String uri = splittedRequestLine[HTTP_URI];
			String httpVersion = splittedRequestLine[HTTP_VERSION];
			log.info("httpMethod: {}, uri: {}, httpVersion: {}", httpMethod, uri, httpVersion);

			Map<String, String> requestHeaders = new HashMap<>();

			line = bufferedReader.readLine();
			while (!line.equals("")) {
				String[] splittedHeaderLine = line.split(HEADER_SPLITTER);
				requestHeaders.put(splittedHeaderLine[HEADER_KEY], splittedHeaderLine[HEADER_VALUE]);
				line = bufferedReader.readLine();
			}

			log.info("requestHeaders: {}", requestHeaders);

			String requestBody;
			if (requestHeaders.containsKey("Content-Length")) {
				int contentLength = Integer.parseInt(requestHeaders.get("Content-Length"));
				char[] bodyChars = new char[contentLength];
				bufferedReader.read(bodyChars, 0, contentLength);
				requestBody = new String(bodyChars);
			} else if (requestHeaders.containsKey("Transfer-Encoding") && requestHeaders.get("Transfer-Encoding")
				.equals("chunked")) {
				StringBuilder chunkRequestBody = new StringBuilder();
				while ((line = bufferedReader.readLine()) != null) {
					int chunkSize = Integer.parseInt(line, 16);
					if (chunkSize == 0) {
						break;
					}
					char[] chunk = new char[chunkSize];
					bufferedReader.read(chunk, 0, chunkSize);
					chunkRequestBody.append(chunk);
					bufferedReader.readLine();
				}
				requestBody = chunkRequestBody.toString();
			}

			// HTTP Response


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
