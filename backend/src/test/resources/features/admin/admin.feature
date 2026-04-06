Feature: Résolution d'incidents par l'admin
  En tant qu'administrateur, je veux résoudre des incidents pour
  maintenir la station.

  Scenario: Un incident bloquant sur un portique doit nécessiter une résolution avant réouverture
    Given An "EQUIPMENT_FAILURE" incident saying "Tourniquer cassé" exists for "gate-1" gate and is closing
    When I try to open the gate while the incident is still unresolved
    Then The system should refuse with an error message "Le portique gate-1 a encore un incident fermant non résolu. Résoudre l'incident avant de réouvir."
    And I resolve the incident
    Then The system opens back the resolved gate
    And I try to re-open the gate manually
    Then I will get an error message "Portique déjà ouvert" from the system telling that it is already open
