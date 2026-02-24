package fr.univcotedazur.skimaster.gate.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Gate {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private GateStatus status;

    @Column(unique = true, nullable = false)
    private String gateCode; //ID métier plus pratique qu'un numéro pour les humains (ex: "G-NORD-01")

    private int threshold;
    private LocalDateTime lastConnexion;

    public Gate(){
    }

    public Gate(String gateCode, int threshold){
        this.gateCode = gateCode;
        this.status = GateStatus.OPEN;
        this.threshold = threshold;
        this.lastConnexion = LocalDateTime.now();
    }

    public int getThreshold() {
        return threshold;
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

    public String getGateCode() {
        return gateCode;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(GateStatus status) {
        this.status = status;
    }

    public void setGateCode(String gateCode) {
        this.gateCode = gateCode;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setLastConnexion(LocalDateTime lastConnexion) {
        this.lastConnexion = lastConnexion;
    }
}
