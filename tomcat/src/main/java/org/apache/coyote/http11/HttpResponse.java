package org.apache.coyote.http11;

import org.apache.coyote.http11.controller.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    //각 path, parameter에 따라 response 만들기

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    int statusCode;
    String responseBody;
    ContentType contentType;


    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public boolean checkResponseBody() {
        return responseBody.isEmpty();
    }

    public String joinResponse() {
        String response = String.join("\r\n",
                "HTTP/1.1 " + statusCode + " OK ",
                "Content-Type: " + contentType.getValue() + ";charset=utf-8 ",
                "Content-Length: " + responseBody.getBytes().length + " ",
                "",
                responseBody);
        return response;
    }
}
