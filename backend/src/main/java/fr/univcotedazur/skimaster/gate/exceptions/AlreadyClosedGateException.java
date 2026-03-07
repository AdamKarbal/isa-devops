package fr.univcotedazur.skimaster.gate.exceptions;

public class AlreadyClosedGateException extends RuntimeException {
    public AlreadyClosedGateException(String message) {
        super(message);
    }
}
