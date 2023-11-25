package org.apache.coyote.http11.Service;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;

import java.util.Optional;

public class RegisterService {
    public boolean register(String account, String password, String email) {
        if(isAlreadyExistAccount(account)) return false;

        User user = new User(account, password, email);
        InMemoryUserRepository.save(user);
        return true;
    }

    // 계정 존재 시 계정 반환, 부재 시 예외 처리
    public boolean isAlreadyExistAccount(String account) {
        Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);
        // todo: exception 수정할 것
        return optionalUser.isPresent();
    }
}
