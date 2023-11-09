package nextstep.jwp.service;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.RequestException;
import nextstep.jwp.model.User;
import org.apache.util.HttpResponseCode;

import java.util.Optional;

public class LoginService {
    // 로그인 여부 boolean을 반환
    public boolean login(String account, String password) {
        User findUser = findUserByAccount(account);
        return findUser.checkPassword(password);
    }

    // 계정 존재 시 계정 반환, 부재 시 예외 처리
    public User findUserByAccount(String account) {
        Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);
        return optionalUser.orElseThrow(() -> new RequestException(HttpResponseCode.USER_NOT_FOUND)) ;
    }
}
