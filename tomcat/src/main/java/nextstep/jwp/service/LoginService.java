package nextstep.jwp.service;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.NotFoundUserException;
import nextstep.jwp.model.User;

public class LoginService {

    private static final LoginService INSTANCE = new LoginService();

    public static LoginService getInstance() {
        return INSTANCE;
    }

    private LoginService() {
    }

    public User findUser(String account, String password) {
        User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(NotFoundUserException::new);
        if (!user.checkPassword(password)) {
            throw new NotFoundUserException();
        }
        return user;
    }
}
