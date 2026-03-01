package fr.univcotedazur.skimaster.pass.components;

import fr.univcotedazur.skimaster.pass.interfaces.PassageUpdate;
import org.springframework.stereotype.Service;

import java.util.List;

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
