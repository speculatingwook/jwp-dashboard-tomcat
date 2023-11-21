package org.apache.coyote.http11.controller;


import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;

import java.util.Optional;

public class LoginHandler {
    private String account;
    private String password;
    private String sessionId;
    public LoginHandler(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public LoginHandler(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean checkUser() {
        if(InMemoryUserRepository.findByAccount(account).isPresent()){
            Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);
            return optionalUser.get().checkPassword(password);
        }
        return false;
    }
}
