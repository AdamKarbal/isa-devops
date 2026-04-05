Feature: Gestion des statistiques de la station
  En tant que gestionnaire, je veux obtenir un rapport quotidien par porte.

  Background:
    Given a gate named "gate-2" exists

  Scenario: : Calcul du rapport journalier pour une porte
    Given the gate "gate-2" was open for 3600 seconds
    And 2 unique skiers passed through "gate-2" today
    When I ask for the daily report of "gate-2"
    Then the report should show 2 total passages
    And the report should show 2 unique skiers
    And the opening time should be "01:00:00"
