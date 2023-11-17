package org.apache.coyote.http11.Service;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;

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
        // todo: exception 수정할 것
        return optionalUser.orElseThrow(() -> new RuntimeException());
    }
}
