package fr.univcotedazur.skimaster.admin.components;

import fr.univcotedazur.skimaster.admin.interfaces.AdminOperations;
import fr.univcotedazur.skimaster.gate.entities.Gate;
import fr.univcotedazur.skimaster.gate.entities.GateStatus;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.gate.repositories.GateRepository;
import fr.univcotedazur.skimaster.inbox.interfaces.MessageInput;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Admin implements AdminOperations {

    @Autowired
    private ManualGateControl manualGateControl;

    @Autowired
    private MessageInput messageInput;

    @Override
    public void setStatus(String code, GateStatus status){
        //TODO: Il faudra effectuer ici une vérif de droits pour l'action sur le portique

        if(status == GateStatus.CLOSED){
            manualGateControl.closeGate(code);
        }
        else{
            manualGateControl.openGate(code);
        }
    }

    @Override
    public void sendMessage(String msg){
        messageInput.sendMessage(msg);
    }

    @Override
    public void clearPannel(){
        messageInput.clearPannel();
    }

    @Override
    public void openAllGates(){
        manualGateControl.openAllGates();
    }

    @Override
    public void closeAllGates(){
        manualGateControl.closeAllGates();
    }
}
