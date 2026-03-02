package fr.univcotedazur.skimaster.cli.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.ArrayList;

public class CliGate {
    @JsonAlias({"gateId", "id", "Id"})
    private String gateId;
    private String status;

    public CliGate(String gateId, String status){
        this.gateId = gateId;
        this.status = status;
    }

    public String getGateId() {
        return gateId;
    }

    public String getStatus() {
        return status;
    }

    public String toString(){
        return "Gate{" +
                "gateId="+ gateId +
                "status="+ status+
                '}';
    }
}
