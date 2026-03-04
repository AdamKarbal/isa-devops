package fr.univcotedazur.skimaster.incident.interfaces;

public interface DisplayNotifier {
    void publishIncidentAlert(String message, String gateCode);
}
