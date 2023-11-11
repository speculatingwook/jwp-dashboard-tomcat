package nextstep.jwp.response;

import java.io.IOException;

public class Response {
	private String contentType;
	private String responseBody;

	protected Response() {
	}

	public Response(String contentType, String responseBody) {
		this.contentType = contentType;
		this.responseBody = responseBody;
	}

	public String getResponse() throws IOException {
		return String.join("\r\n",
			"HTTP/1.1 200 OK ",
			"Content-Type: " + this.contentType + ";charset=utf-8 ",
			"Content-Length: " + this.responseBody.getBytes().length + " ",
			"",
			this.responseBody);
	}
}
