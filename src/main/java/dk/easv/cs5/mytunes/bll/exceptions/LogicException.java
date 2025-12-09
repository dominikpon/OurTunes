package dk.easv.cs5.mytunes.bll.exceptions;

public class LogicException extends Exception {
    public LogicException(String message) {
        super(message);
    }
    public LogicException(String message, Throwable cause) {
        super(message, cause);
    }
}
