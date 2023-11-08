package org.apache.coyote.http11.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.coyote.http11.HttpMethod;

public class HttpRequest {

	private static final String CRLF = "\r\n";
	private static final String REGEX_CRLF = "\\r?\\n";
	private static final String BLANK_SPACE = " ";
	private static final String CONTENT_LENGTH = "Content-Length";
	private static final String TRANSFER_ENCODING = "Transfer-Encoding";
	private HttpMethod httpMethod;
	private String uri;
	private String httpVersion;
	private Map<String, String> requestHeaders;
	private String requestBody;

	private HttpRequest(String requestLine, String requestHeader, String requestBody) {
	}

	public static HttpRequest from(BufferedReader bufferedReader) throws IOException {
		return new HttpRequest(getRequestLine(bufferedReader), getRequestHeader(bufferedReader),
			getRequestBody(bufferedReader));
	}

	private static String getRequestLine(BufferedReader bufferedReader) throws IOException {
		return bufferedReader.readLine();
	}

	private void parseRequestLine(String requestline) {
		String[] splittedRequestLine = requestline.split(BLANK_SPACE);
		this.httpMethod = HttpMethod.valueOf(splittedRequestLine[0]);
		this.uri = splittedRequestLine[1];
		this.httpVersion = splittedRequestLine[2];
	}

	private static String getRequestHeader(BufferedReader bufferedReader) throws IOException {
		StringBuilder requestHeaderBuilder = new StringBuilder();
		String headerLine;
		while ((headerLine = bufferedReader.readLine()) != null && !headerLine.equals("")) {
			requestHeaderBuilder.append(headerLine).append(CRLF);
		}
		return requestHeaderBuilder.toString();
	}

	private void parseRequestHeaders(String requestHeader) {
		this.requestHeaders = Arrays.stream(requestHeader.split(REGEX_CRLF))
			.filter(line -> line.contains(":"))
			.map(line -> line.split(":", 2))
			.collect(Collectors.toMap(
				arr -> arr[0].trim(),
				arr -> arr[1].trim(),
				(value1, value2) -> value1));
	}

	private static String getRequestBody(BufferedReader bufferedReader) throws IOException {
		StringBuilder requestBodyBuilder = new StringBuilder();
		String bodyLine;
		while ((bodyLine = bufferedReader.readLine()) != null) {
			requestBodyBuilder.append(bodyLine).append(CRLF);
		}
		return requestBodyBuilder.toString();
	}
}
