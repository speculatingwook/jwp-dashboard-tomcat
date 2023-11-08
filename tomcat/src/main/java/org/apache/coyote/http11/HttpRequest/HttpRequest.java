package org.apache.coyote.http11.HttpRequest;

public class HttpRequest {

	private final String requestLine;
	private final String requestHeader;
	private final String requestBody;

	private HttpRequest(String requestLine, String requestHeader, String requestBody) {
		this.requestLine = requestLine;
		this.requestHeader = requestHeader;
		this.requestBody = requestBody;
	}

	public static HttpRequest of(String httpMethod, String uri, String httpVersion) {
		return new HttpRequest(httpMethod, uri, httpVersion);
	}
}
