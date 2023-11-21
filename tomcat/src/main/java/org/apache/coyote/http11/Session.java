package org.apache.coyote.http11;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;

import java.util.UUID;

public class Session {
    private static String JSESSIONID = "656cef62-e3c4-40bc-a8df-94732920ed46";
    private User user;
    private final String sessionId;
    private boolean isSessionValid = false;

    public Session(String sessionId) {
        this.sessionId = sessionId;
        this.isSessionValid = isSessionIDExistsInUserRepository();
        System.out.println(isSessionValid);
        if (isSessionValid) {
            getUserBySessionId();
        }
    }

    private void getUserBySessionId() {
        this.user = InMemoryUserRepository.findBySessionId(sessionId);
    }

    public boolean isSessionValid() {
        return isSessionValid;
    }
    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public boolean isSessionIDExistsInUserRepository() {
        if (InMemoryUserRepository.findBySessionId(sessionId) == null) {
            return false;
        }
        return true;
    }

    public User getUser() {
        return user;
    }
}
