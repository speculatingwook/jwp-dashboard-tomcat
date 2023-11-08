package org.apache.coyote.http11.login;


import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;

import java.util.Optional;

public class LoginHandler {
    private final String account;
    private final String password;
    public LoginHandler(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public boolean checkUser() {
        if(InMemoryUserRepository.findByAccount(account).isPresent()){
            Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);
            return optionalUser.get().checkPassword(password);
        }
        return false;
    }
}
