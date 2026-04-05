Feature: Signalement des incidents
  En tant que pisteur, je veux signaler des problèmes pour sécuriser la station.

  Scenario: Un incident bloquant sur un portique doit le fermer
    Given No incidents exist in the database
    When I report a blocking "EQUIPMENT_FAILURE" incident on gate "gate-2" with description "Moteur bloqué"
    Then the database should contain 1 incident
    And the gate "gate-2" should receive a close command
