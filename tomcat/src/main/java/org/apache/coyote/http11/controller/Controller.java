package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.HttpRequest.HttpRequest;
import org.apache.coyote.http11.HttpResponse.HttpResponse;

public interface Controller {
	HttpResponse handleRequest(HttpRequest httpRequest);
}
