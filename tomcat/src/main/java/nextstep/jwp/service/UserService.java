package nextstep.jwp.service;

import java.util.Optional;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.NotFoundUserException;
import nextstep.jwp.model.User;

public class UserService {

    private static final UserService INSTANCE = new UserService();

    public static UserService getInstance() {
        return INSTANCE;
    }

    private UserService() {
    }

    public User findUser(String account, String password) {
        User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(NotFoundUserException::new);
        if (!user.checkPassword(password)) {
            throw new NotFoundUserException();
        }
        return user;
    }

    public void registerUser(String account, String email, String password) {
        Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 계정입니다.");
        }
        User user = new User(account, password, email);
        InMemoryUserRepository.save(user);
    }
}
