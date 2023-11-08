package org.apache.coyote.http11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

			HttpMethod httpMethod;
			try {
				httpMethod = HttpMethod.valueOf(splittedRequestLine[HTTP_METHOD]);
			} catch (IllegalArgumentException e) {
				httpMethod = null;
			}
			String uri = splittedRequestLine[HTTP_URI];

			// if `uri` contains `?`
			Map<String, String> queryParams = new HashMap<>();
			if (uri.contains("?")) {
				String[] splittedQuery = uri.split("\\?")[1].split("\\&");
				uri = uri.split("\\?")[0];
				for (int i = 0; i < splittedQuery.length; i++) {
					String[] values = splittedQuery[i].split("\\=");
					queryParams.put(values[0], values[1]);
				}
			}

			log.info("queryParams: {}", queryParams);

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
