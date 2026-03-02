package fr.univcotedazur.skimaster.gate.components;

import fr.univcotedazur.skimaster.gate.dtos.AccessRequest;
import fr.univcotedazur.skimaster.gate.entities.Gate;
import fr.univcotedazur.skimaster.gate.entities.GateStatus;
import fr.univcotedazur.skimaster.gate.enums.BeepSignal;
import fr.univcotedazur.skimaster.gate.exceptions.AlreadyClosedGateException;
import fr.univcotedazur.skimaster.gate.exceptions.AlreadyOpenGateException;
import fr.univcotedazur.skimaster.gate.exceptions.NoGateExistentException;
import fr.univcotedazur.skimaster.gate.interfaces.ManualGateControl;
import fr.univcotedazur.skimaster.gate.interfaces.PortierInformation;
import fr.univcotedazur.skimaster.gate.repositories.GateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Portier implements ManualGateControl, PortierInformation {
    @Autowired
    private GateRepository repository;

    @Transactional
    public void closeGate(String code){
        Gate gate = repository.findByGateCode(code).orElseThrow(() ->
                new IllegalArgumentException("Cannot find gate with code :" + code));
        if(gate.getStatus() == GateStatus.CLOSED) throw new AlreadyClosedGateException("Gate of code : " + code + "is already closed.");
        gate.setStatus(GateStatus.CLOSED);
    }

    @Transactional
    public void openGate(String code){
        Gate gate = repository.findByGateCode(code).orElseThrow(() ->
                new IllegalArgumentException("Cannot find gate with code :" + code));
        if(gate.getStatus() == GateStatus.OPEN) throw new AlreadyOpenGateException("Gate of code : " + code + "is already open.");
        gate.setStatus(GateStatus.OPEN);
    }

    @Transactional
    public void openAllGates(){
        List<Gate> allGates = repository.findAll();
        if(allGates.isEmpty()) throw new NoGateExistentException("No gate found in the database. This is unexpected.");
        for(Gate g : allGates) g.setStatus(GateStatus.OPEN);
}
    @Override
    public AccessRequest authorizePassage(Long forfeitId, BeepSignal signal) {
    //check gate Opened
        return new AccessRequest(forfeitId,signal,true);
    }

    @Transactional
    public void closeAllGates(){
        List<Gate> allGates = repository.findAll();
        if(allGates.isEmpty()) throw new NoGateExistentException("No gate found in the database. This is unexpected.");
        for(Gate g : allGates) g.setStatus(GateStatus.CLOSED);
    }

    @Override
    public AccessRequest denyPassage() {
    //same
        return new AccessRequest(null,BeepSignal.ACCESS_DENIED,false);
    }
}
