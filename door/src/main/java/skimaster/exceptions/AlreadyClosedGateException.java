package skimaster.exceptions;

public class AlreadyClosedGateException extends RuntimeException {
    public AlreadyClosedGateException(String message) {
        super(message);
    }
}
