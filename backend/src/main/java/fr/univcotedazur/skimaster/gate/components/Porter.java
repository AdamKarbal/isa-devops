package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.gate.interfaces.PassageUpdate;
import org.springframework.stereotype.Service;

@Service
public class Porter implements PassageUpdate {

    private final PassService passService;

    public Porter(PassService passService) {
        this.passService = passService;
    }

    @Override
    public void processPassageRequest(Long nfcCardId) {
        passService.processPassages(java.util.List.of(nfcCardId));
    }
}
