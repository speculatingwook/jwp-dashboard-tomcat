package nextstep.exception;

public class NotFoundControllerException extends RuntimeException{

    public NotFoundControllerException() {
    }

    public NotFoundControllerException(String message) {
        super(message);
    }
}
