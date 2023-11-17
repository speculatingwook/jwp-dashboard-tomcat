package org.apache.coyote.http11.handler;

import java.util.Map;
import org.apache.coyote.http11.httprequest.HttpRequest;
import org.apache.coyote.http11.httpresponse.HttpResponse;
public class RequestHandler {

	private HttpRequest httpRequest;
	private HttpResponse httpResponse;
	private Map<String, Method> uriToMethodMap;

}
