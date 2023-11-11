package nextstep.jwp.request;

import java.util.HashMap;

public class QueryString {
	private String queryString;

	protected QueryString() {
	}

	public QueryString(String queryString) {
		this.queryString = queryString;
	}

	public HashMap<String, String> toParsing() {
		HashMap<String, String> parsedQueryString = new HashMap<>();
		String[] split = queryString.split("&");
		String[] account = split[0].split("=");
		parsedQueryString.put(account[0], account[1]);

		String[] password = split[1].split("=");
		parsedQueryString.put(password[0], password[1]);

		return parsedQueryString;
	}

}
