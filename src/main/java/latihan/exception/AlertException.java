package latihan.exception;  // adjust to your package structure

public class AlertException extends RuntimeException {
    public AlertException(String message) {
        super(message);
    }
}