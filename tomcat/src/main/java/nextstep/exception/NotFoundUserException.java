package nextstep.exception;

public class NotFoundUserException extends RuntimeException{
    public NotFoundUserException() {
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}
