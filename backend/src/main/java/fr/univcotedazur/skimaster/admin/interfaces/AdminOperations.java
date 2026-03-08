package fr.univcotedazur.skimaster.admin.interfaces;

public interface AdminOperations {
    void setStatus(String code, String status);
    void sendMessage(String msg);
    void clearPannel();
    void openAllGates();
    void closeAllGates();
}
