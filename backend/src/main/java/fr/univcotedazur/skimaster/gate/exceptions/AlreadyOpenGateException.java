package fr.univcotedazur.skimaster.gate.exceptions;

public class AlreadyOpenGateException extends RuntimeException {
    public AlreadyOpenGateException(String message) {
        super(message);
    }
}
