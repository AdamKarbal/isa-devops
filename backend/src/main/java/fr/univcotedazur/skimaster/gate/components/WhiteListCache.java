package fr.univcotedazur.skimaster.gate.components;


import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WhiteListCache {

    private List<Forfait> forfeitList = new ArrayList<>();
public WhiteListCache() {

}
    public List<Forfait> getForfeitList() {
        return forfeitList;
    }

    public void addForfeits(List<Forfait> forfeitToAdd){
        forfeitList.clear();
        forfeitList.addAll(forfeitToAdd);
    }

    public List<Long> getValidForfeitIds() {
        return forfeitList.stream().map(Forfait::getId).toList();
    }
}
