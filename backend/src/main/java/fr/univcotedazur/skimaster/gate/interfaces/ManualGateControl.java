package fr.univcotedazur.skimaster.gate.interfaces;

import fr.univcotedazur.skimaster.gate.connectors.externaldto.GateStatusResponseDTO;

import java.util.List;

public interface ManualGateControl {
    void closeGate(String code);
    void openGate(String code);
    void closeAllGates();
    void openAllGates();
    GateStatusResponseDTO gateStatus(String code);
    void setAlertThreshold(String code, int alertThreshold);
    List<String> getConfiguredGateCodes();
}
