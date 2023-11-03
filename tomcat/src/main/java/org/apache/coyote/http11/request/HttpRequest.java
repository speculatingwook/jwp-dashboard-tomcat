package org.apache.coyote.http11.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class HttpRequest {

	private String method;
	private String path;
	private Map<String, String> headers;
	private String body;

	public static HttpRequest parse(BufferedReader bufferedReader) throws IOException {

		HttpRequest request = new HttpRequest();

		String requestLine = bufferedReader.readLine();
		String[] requestParts = requestLine.split(" ");
		request.method = requestParts[0];
		request.path = requestParts[1];

		String line;
		request.headers = new HashMap<>();
		while (!(line = bufferedReader.readLine()).isEmpty()) {
			String[] headerParts = line.split(": ");
			request.headers.put(headerParts[0], headerParts[1]);
		}

		if ("POST".equals(request.method) && request.headers.containsKey("Content-Length")) {
			int contentLength = Integer.parseInt(request.headers.get("Content-Length"));
			char[] bodyChars = new char[contentLength];
			bufferedReader.read(bodyChars, 0, contentLength);
			request.body = new String(bodyChars);
		}

		return request;
	}

}
