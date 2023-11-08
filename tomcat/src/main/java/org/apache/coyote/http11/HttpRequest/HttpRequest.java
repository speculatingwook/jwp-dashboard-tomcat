package org.apache.coyote.http11.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {

	private static final String CRLF = "\r\n";

	private final String requestLine;
	private final String requestHeader;
	private final String requestBody;

	private HttpRequest(String requestLine, String requestHeader, String requestBody) {
		this.requestLine = requestLine;
		this.requestHeader = requestHeader;
		this.requestBody = requestBody;
	}

	public static HttpRequest from(BufferedReader bufferedReader) throws IOException {
		return new HttpRequest(getRequestLine(bufferedReader), getRequestHeader(bufferedReader),
			getRequestBody(bufferedReader));
	}

	private static String getRequestLine(BufferedReader bufferedReader) throws IOException {
		return bufferedReader.readLine();
	}

	private static String getRequestHeader(BufferedReader bufferedReader) throws IOException {
		StringBuilder requestHeaderBuilder = new StringBuilder();
		String headerLine;
		while ((headerLine = bufferedReader.readLine()) != null && !headerLine.equals("")) {
			requestHeaderBuilder.append(headerLine).append(CRLF);
		}
		return requestHeaderBuilder.toString();
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
