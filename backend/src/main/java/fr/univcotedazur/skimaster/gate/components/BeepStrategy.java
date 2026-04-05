package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.interfaces.BeepInformation;
import org.springframework.stereotype.Service;

@Service
public class BeepStrategy implements BeepInformation {

    public BeepStrategy() {
    }

    @Override
    public BeepSignal processBeepInformation(AgeCategory age) {
         return  age.equals(AgeCategory.CHILD)?BeepSignal.CHILD_BEEP:BeepSignal.STANDARD_BEEP;
    }

}
