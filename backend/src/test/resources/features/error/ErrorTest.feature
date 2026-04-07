Feature: Test error scenarios

  Background:
    Given a client named "John" with the credit card "1234567890"
    And an adult dayly forfait with a price of 10
    And the client gets the forfait
    And a closed gate

  Scenario: Attempting to register twice
    When a client tries to register a second time
    Then a error idicating they are already registered is received

  Scenario: Attempting to find an unregistered client
    When looking for an unregistered client with id 50
    Then an error indicating the client can't be found is received
    
  Scenario: Attempting to validate an empty cart
    When a client tries to validate an empty cart
    Then an error indicating the cart is empty is received

  Scenario: Attempting to remove more item than what is in the cart
    When a client put 1 product in his empty cart
    And tries to remove 2 products from his cart
    Then an error indicating the quantity of items to remove is wrong is received
