package fr.univcotedazur.skimaster.gate.interfaces;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.gate.dtos.BeepContext;

public interface BeepInformation {
    
    void assignBeep(BeepContext beep, AgeCategory age);
}
