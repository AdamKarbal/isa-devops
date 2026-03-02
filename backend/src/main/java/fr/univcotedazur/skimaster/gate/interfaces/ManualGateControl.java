package fr.univcotedazur.skimaster.gate.interfaces;

public interface ManualGateControl {
    void closeGate(String code);
    void openGate(String code);
    void closeAllGates();
    void openAllGates();
}
