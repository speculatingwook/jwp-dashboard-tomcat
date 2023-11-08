package org.apache.coyote.http11.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {

	private final String requestLine;
	private final String requestHeader;
	private final String requestBody;

	private HttpRequest(String requestLine, String requestHeader, String requestBody) {
		this.requestLine = requestLine;
		this.requestHeader = requestHeader;
		this.requestBody = requestBody;
	}

	public static HttpRequest from(BufferedReader bufferedReader) throws IOException {

	}

	private static String getRequestLine(BufferedReader bufferedReader) throws IOException {
		return bufferedReader.readLine();
	}

	private static String getRequestHeader(BufferedReader bufferedReader) throws IOException {
		StringBuilder requestHeaderBuilder = new StringBuilder();
		String headerLine;
		while ((headerLine = bufferedReader.readLine()) != null && !headerLine.equals("")) {
			requestHeaderBuilder.append(headerLine).append("\r\n");
		}
		return requestHeaderBuilder.toString();
	}
}
