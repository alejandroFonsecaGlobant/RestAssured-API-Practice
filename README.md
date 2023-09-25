
# RestAssured API Testing Practice

Practice project using RestAssured with Java to test MockAPI's endpoints related to Clients and Resources


## Technologies

* Java (17)
* Maven
* RestAssured
* Cucumber

## Project Setup
1. Install Java SDK and IntelliJ
2. Open the project with IntelliJ
3. Build the project with Maven
4. Run the TestRunner class
5. Run the features to execute the tests


## Test Cases
```
@active
Feature: Client testing CRUD

  @smoke
  Scenario: View all the clients
    Given there are registered clients in the system
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
```

```
@active
Feature: Resource testing CRUD

  @smoke
  Scenario: View all the resources
    Given there are registered resources in the system
    When I send a GET request to view all the resources
    Then the resource response should have a status code of 200
    And validates the response with resource list JSON schema

  @smoke
  Scenario: Update the last resource
    Given there are registered resources in the system
    And I retrieve the details of the latest resource
    When I send a PUT request to update the latest resource
    """
    {
      "name": "NewName",
      "trademark": "NewTradeMark",
      "stock": 1000,
      "price": 99.99,
      "description": "description",
      "tags": "NewTag",
      "isActive": true
    }
    """
    Then the resource response should have a status code of 200
    And the response should have the following details:
      | Name    | Trademark        | Stock  | Price      | Description | Tags   | Is_active |
      | NewName | NewTradeMark     | 1000   | 99.99      | description | NewTag | true      |
    And validates the response with the resource JSON schema
```
## Endpoints
**Base URL:** https://63b6dfe11907f863aa04ff81.mockapi.io

### Client endpoints
**[GET] all clients:** Base URL + /api/v1/clients
**[POST] create a client:** Base URL + /api/v1/clients

### Resource endpoints
**[GET] all resources:** Base URL + /api/v1/resources
**[POST] create a resource:** Base URL + /api/v1/resources  
**[PUT] update a resource:** Base URL + /api/v1/resources/{id}
## Entities

### Client

* name: String
* lastName: String
* country: String
* city: String
* email: String
* phone: String
* id: String

### Resource

* name: String
* trademark: String
* stock: int
* price: float
* description: String
* tags: String
* isActive: boolean
* id: String
## Author

- [@alejandrofonsecaglobant](https://www.github.com/alejandrofonsecaglobant)

