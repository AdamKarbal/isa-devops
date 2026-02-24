package fr.univcotedazur.skimaster.forfait.exceptions;

public class ForfaitIdNotFoundException extends RuntimeException {
    private Long id;

    public ForfaitIdNotFoundException(Long id) {
        super("Forfait not found: " + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
