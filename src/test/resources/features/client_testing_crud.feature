@active
Feature: Client testing CRUD

  @smoke
  Scenario: View all the clients
    Given there are 3 registered clients in the system
    When I send a GET request to view all the clients
    Then the client response should have a status code of 200
    And validates the response with client list JSON schema

  @smoke
  Scenario: Create a new client
    Given I have a client with the following details:
      | Name | LastName | Country | City     | Email             | Phone         |
      | name | lastName | country | city     | email@email.com   | 123-456-7890  |
    When I send a POST request to create a client
    Then the client response should have a status code of 201
    And the response should include the details of the created client
    And validates the response with client JSON schema
