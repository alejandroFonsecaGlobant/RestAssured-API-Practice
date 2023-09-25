package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.models.Resource;
import com.testing.api.requests.ClientRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);

    private final ClientRequest clientRequest = new ClientRequest();

    private Response response;
    private Client   client;


    @Given("there are 3 registered clients in the system")
    public void there_are_3_registered_clients_in_the_system() {
        response = clientRequest.getClients();
        logger.info(response.jsonPath().prettify());
        Assert.assertEquals(200, response.statusCode());

        List<Client> clientList = clientRequest.getClientsEntity(response);
        while (clientList.size() < 3) {
            response = clientRequest.createDefaultClient();
            logger.info(response.statusCode());
            Assert.assertEquals(201, response.statusCode());
        }
    }

    @Given("I have a client with the following details:")
    public void i_have_a_client_with_the_following_details(DataTable clientData) {
        Map<String, String> clientDataMap = clientData.asMaps().get(0);

        client = Client.builder().name(clientDataMap.get("Name"))
                                 .lastName(clientDataMap.get("LastName"))
                                 .country(clientDataMap.get("Country"))
                                 .city(clientDataMap.get("City"))
                                 .email(clientDataMap.get("Email"))
                                 .phone(clientDataMap.get("Phone"))
                                 .build();
        logger.info("Client mapped:" + client);
    }

    @When("I send a GET request to view all the clients")
    public void i_send_a_get_request_to_view_all_the_clients() {
        response = clientRequest.getClients();
    }

    @When("I send a POST request to create a client")
    public void i_send_a_post_request_to_create_a_client() {
        response = clientRequest.createClient(client);
    }

    @Then("the client response should have a status code of {int}")
    public void the_response_should_have_a_status_code_of(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @Then("the response should include the details of the created client")
    public void the_response_should_include_the_details_of_the_created_client() {
        Client createdClient = clientRequest.getClientEntity(response);
        createdClient.setId(null);
        Assert.assertEquals(createdClient, client);
    }

    @Then("validates the response with client JSON schema")
    public void validates_the_response_with_client_json_schema() {
        System.out.println(response.toString());
        String path = "schemas/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Client schema validated with object");
    }

    @Then("validates the response with client list JSON schema")
    public void validates_the_response_with_client_list_json_schema() {
        System.out.println(response.toString());
        String path = "schemas/clientListSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Client list schema validated with object");
    }
}
