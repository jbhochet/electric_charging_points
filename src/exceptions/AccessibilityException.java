package exceptions;

public class AccessibilityException extends Exception {
    public AccessibilityException(String message, Throwable err) {
        super(message, err);
    }
}
