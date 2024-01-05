package uom.backend.physioassistant.exceptions;

public class AlreadyAddedException extends RuntimeException {
    public AlreadyAddedException(String message) {
        super(message);
    }
}
