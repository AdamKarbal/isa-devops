package skimaster.entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Gate {

    private final String gateId;
    private GateStatus status;
    private int alertThreshold;
    private int currentPassages;
    private final List<Passage> passages;
    private final List<Incident> incidents;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Gate(String gateId, GateStatus status, int alertThreshold, LocalTime startTime,LocalTime endTime) {
        this.gateId = gateId;
        this.status = status;
        this.alertThreshold = alertThreshold;
        this.currentPassages = 0;
        this.startTime = startTime;
        this.endTime = endTime;
        this.passages = new ArrayList<>();
        this.incidents = new ArrayList<>();
    }

    public String getGateId() {
        return gateId;
    }

    public GateStatus getStatus() {
        return status;
    }

    public void setStatus(GateStatus status) {
        this.status = status;
    }

    public int getAlertThreshold() {
        return alertThreshold;
    }

    public void setAlertThreshold(int alertThreshold) {
        this.alertThreshold = alertThreshold;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getCurrentPassages() {
        return currentPassages;
    }

    public List<Passage> getPassages() {
        return passages;
    }

    public List<Incident> getIncidents() {
        return incidents;
    }

    public void registerPassage(Passage passage) {
        passages.add(passage);
        if (passage.isAccepted()) {
            currentPassages++;
        }
    }

    public void registerIncident(Incident incident) {
        incidents.add(incident);
    }
}
