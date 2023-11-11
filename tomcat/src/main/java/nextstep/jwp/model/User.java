package nextstep.jwp.model;

public class User {

    private final Long id;
    private final String account;
    private final String password;
    private final String email;
    private final String JSESSIONID;

    public User(Long id, String account, String password, String email, String sessionId) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
        this.JSESSIONID = sessionId;
    }

    public User(String account, String password, String email, String sessionId) {
        this(null, account, password, email, sessionId);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getAccount() {
        return account;
    }

    public String getSessionId() {
        return JSESSIONID;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ",JSSESIONID='" + JSESSIONID + '\'' +
                '}';
    }
}
