package org.apache.coyote.http11.httpresponse;

import java.util.Map;

import org.apache.coyote.http11.common.HttpStatus;

import lombok.Getter;

@Getter
public class HttpResponse {

	private String httpVersion;
	private HttpStatus httpStatus;
	private Map<String, String> responseHeaders;
	private String responseBody = null;

}
