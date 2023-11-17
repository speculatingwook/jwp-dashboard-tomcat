package org.apache.coyote.http11.httprequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.coyote.http11.common.HttpMethod;

import lombok.Getter;

@Getter
public class HttpRequest {

	private static final String CRLF = "\r\n";
	private static final String REGEX_CRLF = "\\r?\\n";
	private static final String BLANK_SPACE = " ";
	private static final String CONTENT_LENGTH = "Content-Length";
	private static final String TRANSFER_ENCODING = "Transfer-Encoding";
	private static final String CHUNKED = "chunked";
	private HttpMethod httpMethod;
	private String uri;
	private String httpVersion;
	private Map<String, String> requestHeaders;
	private String requestBody = null;

	private HttpRequest(BufferedReader bufferedReader) throws IOException {
		parseRequestLine(bufferedReader);
		parseRequestHeaders(bufferedReader);
		if (requestHeaders.containsKey(CONTENT_LENGTH)) {
			parseContentLengthRequestBody(bufferedReader);
		} else if (requestHeaders.containsKey(TRANSFER_ENCODING)) {
			parseTransferEncodingRequestBody(bufferedReader);
		}
	}

	public static HttpRequest from(BufferedReader bufferedReader) throws IOException {
		return new HttpRequest(bufferedReader);
	}

	private static String getRequestLine(BufferedReader bufferedReader) throws IOException {
		return bufferedReader.readLine();
	}

	private void parseRequestLine(BufferedReader bufferedReader) throws IOException {
		String requestLine = getRequestLine(bufferedReader);
		String[] splittedRequestLine = requestLine.split(BLANK_SPACE);
		this.httpMethod = HttpMethod.valueOf(splittedRequestLine[0]);
		this.uri = splittedRequestLine[1];
		this.httpVersion = splittedRequestLine[2];
	}

	private static String getRequestHeader(BufferedReader bufferedReader) throws IOException {
		StringBuilder requestHeaderBuilder = new StringBuilder();
		String headerLine;
		while ((headerLine = bufferedReader.readLine()) != null && !headerLine.isEmpty()) {
			requestHeaderBuilder.append(headerLine).append(CRLF);
		}
		return requestHeaderBuilder.toString();
	}

	private void parseRequestHeaders(BufferedReader bufferedReader) throws IOException {
		String requestHeader = getRequestHeader(bufferedReader);
		this.requestHeaders = Arrays.stream(requestHeader.split(REGEX_CRLF))
			.filter(line -> line.contains(":"))
			.map(line -> line.split(":", 2))
			.collect(Collectors.toMap(
				arr -> arr[0].trim(),
				arr -> arr[1].trim(),
				(value1, value2) -> value1));
	}

	private void parseContentLengthRequestBody(BufferedReader bufferedReader) throws IOException {
		int contentLength = Integer.parseInt(requestHeaders.get(CONTENT_LENGTH));
		char[] charRequestBody = new char[contentLength];
		int bytesRead = bufferedReader.read(charRequestBody, 0, contentLength);
		this.requestBody = new String(charRequestBody, 0, bytesRead);
	}

	private void parseTransferEncodingRequestBody(BufferedReader bufferedReader) throws IOException {
		StringBuilder requestBodyBuilder = new StringBuilder();
		String chunkSizeLine;
		while (!(chunkSizeLine = bufferedReader.readLine()).equals("0")) {
			int chunkSize = Integer.parseInt(chunkSizeLine, 16);
			char[] chunkData = new char[chunkSize];
			int bytesRead = bufferedReader.read(chunkData, 0, chunkSize);
			requestBodyBuilder.append(chunkData, 0, bytesRead);
			bufferedReader.readLine();
		}
		bufferedReader.readLine();
		this.requestBody = requestBodyBuilder.toString();
	}
}
