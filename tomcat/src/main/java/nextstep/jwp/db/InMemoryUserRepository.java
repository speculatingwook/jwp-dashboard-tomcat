package nextstep.jwp.db;

import nextstep.jwp.model.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    static {
        final User user = new User(1L, "gugu", "password", "hkkang@woowahan.com", "656cef62-e3c4-40bc-a8df-94732920ed46");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        database.put(user.getAccount(), user);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    public static User findBySessionId(String sessionId) {
        for (Map.Entry<String, User> entry : database.entrySet()) {
            User user = entry.getValue();
            if (user.getSessionId().equals(sessionId)) {
                return user;
            }
        }
        return null;
    }

    private InMemoryUserRepository() {}
}
