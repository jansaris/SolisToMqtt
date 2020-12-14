package solis2mqtt.exceptions;

public abstract class SolisException extends Exception {
    public SolisException(String message, Exception cause) {
        super(message, cause);
    }
    public SolisException(String message) {
        super(message);
    }
}
