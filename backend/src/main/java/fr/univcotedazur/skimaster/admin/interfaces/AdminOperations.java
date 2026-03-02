package fr.univcotedazur.skimaster.admin.interfaces;
import fr.univcotedazur.skimaster.gate.entities.GateStatus;

public interface AdminOperations {
    void setStatus(String code, GateStatus status);
    void sendMessage(String msg);
    void clearPannel();
    void openAllGates();
    void closeAllGates();
}
