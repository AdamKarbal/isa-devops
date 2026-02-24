package fr.univcotedazur.skimaster.gate.components;


import fr.univcotedazur.skimaster.gate.entities.Forfeit;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WhiteListCache {

    private List<Forfeit> forfeitList = new ArrayList<>();
public WhiteListCache() {

}
    public List<Forfeit> getForfeitList() {
        return forfeitList;
    }

    public void addForfeits(List<Forfeit> forfeitToAdd){
        forfeitList.addAll(forfeitToAdd);
    }
}
