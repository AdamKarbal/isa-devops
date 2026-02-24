package fr.univcotedazur.skimaster.gate.entities;

import fr.univcotedazur.skimaster.gate.enums.GateStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Gate {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private GateStatus status;

    @NotNull
    private int threshold;

    @NotBlank
    private LocalDateTime lastConnexion;

    @OneToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "gate")
    private List<Passage> passageList = new ArrayList<>();

    public Gate() {}
    public Gate(Long id, GateStatus status, int threshold, LocalDateTime lastConnexion,List<Passage> passageList) {
        this.id = id;
        this.status = status;
        this.threshold = threshold;
        this.lastConnexion = lastConnexion;
        this.passageList = passageList;
    }

    public Long getId() {
        return id;
    }

    public GateStatus getStatus() {
        return status;
    }

    public LocalDateTime getLastConnexion() {
        return lastConnexion;
    }

    public int getThreshold() {
        return threshold;
    }

    public List<Passage> getPassageList() {
        return passageList;
    }

    public void addPassage(Passage passage) {
        passageList.add(passage);
    }
    public void removePassage(Passage passage) {
        passageList.remove(passage);
    }
}
