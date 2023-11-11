package nextstep.jwp.request;

import java.io.IOException;

public class Request {
	private String path;

	public Request(String path) {
		this.path = path;
	}

	public String getFileName() throws IOException {
		String fileName = path;
		if (path.contains("?")) {
		    fileName = path.split("[?]")[0];
		}
		return fileName;
	}

	public String getQueryString() throws IOException {
		if (path.contains("?")) {
			return path.split("[?]")[1];
		}
		return "";
	}
}
