package dk.easv.cs5.mytunes.bll.exceptions;

public class DuplicateSongException extends Exception {
    public DuplicateSongException(String message) {
        super(message);
    }
}
