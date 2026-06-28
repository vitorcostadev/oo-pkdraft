package domain.exceptions;

public class BadJsonFormatException extends RuntimeException {
    public BadJsonFormatException(String message) {
        super(message);
    }
}
