package nextstep.jwp.exception.notfound;

public class NotFoundUserException extends RuntimeException {

    public NotFoundUserException() {
        super("유저를 조회할 수 없습니다.");
    }
}
