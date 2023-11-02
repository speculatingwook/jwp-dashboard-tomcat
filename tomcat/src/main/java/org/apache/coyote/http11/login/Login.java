package org.apache.coyote.http11.login;


import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;

import java.util.Optional;

public class Login {
    private final String account;
    private final String password;
    public Login(String account, String password) {
        this.account = account;
        this.password = password;
    }



    public String getUserInfo() {
        if(InMemoryUserRepository.findByAccount(account).isPresent()){
            Optional<User> optionalUser = InMemoryUserRepository.findByAccount(account);
            User user = optionalUser.get();
            if(user.checkPassword(password)){
                return user.toString();
            }
        }
        return "잘못된 정보입니다.";
    }
}
