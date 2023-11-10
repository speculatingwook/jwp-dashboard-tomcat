package org.apache.coyote.http11.Session;

import nextstep.jwp.model.User;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private static Map<String, User> sessionData = new HashMap<>();

    public static void loginUser(String sessionId, User user) {
        sessionData.put(sessionId, user);
    }

    public static User getUser(String sessionId) {
        return sessionData.get(sessionId);
    }

    public static void logoutUser(String sessionId) {
        sessionData.remove(sessionId);
    }
}
