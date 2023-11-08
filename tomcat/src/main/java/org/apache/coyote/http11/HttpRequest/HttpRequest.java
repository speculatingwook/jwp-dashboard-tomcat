package org.apache.coyote.http11.HttpRequest;

public class HttpRequest {

	private final String httpMethod;
	private final String uri;
	private final String httpVersion;

	private HttpRequest(String httpMethod, String uri, String httpVersion) {
		this.httpMethod = httpMethod;
		this.uri = uri;
		this.httpVersion = httpVersion;
	}

	public static HttpRequest of(String httpMethod, String uri, String httpVersion) {
		return new HttpRequest(httpMethod, uri, httpVersion);
	}
}
