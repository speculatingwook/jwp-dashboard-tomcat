package nextstep.jwp.exception.badrequest;

public class ExistUserException extends RuntimeException {

    public ExistUserException() {
        super("이미 존재하는 회원입니다. 회원가입할 수 없습니다.");
    }
}
