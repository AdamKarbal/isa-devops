package fr.univcotedazur.skimaster.gate.components;

import org.springframework.stereotype.Component;

@Component
public class GateRegistry {
/*
    @Autowired
    private GateRepository repository;

    @Transactional
    public Gate registerGate(String code, int threshold){
        if(repository.findByGateCode(code).isPresent()){
            throw new AlreadyExistantGateException("This gate with "+ code  +" code already exists.");
        }
        Gate newGate = new Gate(code, threshold);
        return repository.save(newGate);
    }

    @Transactional
    public void updateConnection(String code){
        Gate gate = repository.findByGateCode(code).orElseThrow(()-> new IllegalArgumentException("Cannot find gate with code : " + code));
        gate.setLastConnexion(LocalDateTime.now());
    }
*/
}
