package fr.univcotedazur.skimaster.gate.components;


import fr.univcotedazur.skimaster.forfait.entities.Forfeit;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WhiteListCache {

    private List<Forfeit> forfeitList = new ArrayList<>();
public WhiteListCache() {

}
    public List<Forfeit> getForfeitList() {
        return forfeitList;
    }

    public void addForfeits(List<Forfeit> forfeitToAdd){
        forfeitList.clear();
        forfeitList.addAll(forfeitToAdd);
    }

    public List<Long> getValidForfeitIds() {
        return forfeitList.stream().map(Forfeit::getId).toList();
    }
}
