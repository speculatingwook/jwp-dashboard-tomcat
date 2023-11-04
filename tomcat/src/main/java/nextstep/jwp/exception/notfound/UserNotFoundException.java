package nextstep.jwp.exception.notfound;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("우저를 조회할 수 없습니다.");
    }
}
