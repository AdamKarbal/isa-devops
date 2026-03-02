package fr.univcotedazur.skimaster.gate.interfaces;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;

public interface BeepInformation {
    
    AccessRequest assignAuthorizationBeep(BeepContext beep, AgeCategory age);
    AccessRequest assignForbiddenBeep(BeepContext beep);
}
