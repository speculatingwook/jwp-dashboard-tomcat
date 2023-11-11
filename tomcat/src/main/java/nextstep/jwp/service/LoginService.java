package nextstep.jwp.service;

import java.util.HashMap;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;

public class LoginService {

	protected LoginService() {
	}

	public static String login(HashMap<String, String> parsedQueryString) {
		User user = InMemoryUserRepository.findByAccount(parsedQueryString.get("account"))
			.orElseThrow(() -> new IllegalArgumentException());

		if (!user.checkPassword(parsedQueryString.get("password"))) {
			return "/login.html";
		}
		return user.toString();
	}
}
