package com.testing.api.requests;

import com.google.gson.Gson;
import com.testing.api.models.Client;
import com.testing.api.utils.Constants;
import com.testing.api.utils.JsonFileReader;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClientRequest extends BaseRequest {
    private String endpoint;

    /**
     * This is a function to get all the clients
     * @return Response of the request
     */
    public Response getClients() {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * This is a function to create a new client
     * @param client Client to create
     * @return Response of the request
     */
    public Response createClient(Client client) {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestPost(endpoint, createBaseHeaders(), client);
    }

    public Response deleteClient(int clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestDelete(endpoint, createBaseHeaders());
    }

    /**
     * This is a function to obtain a Client class from a response
     * @param response The response from a request
     * @return A client class
     */
    public Client getClientEntity(@NotNull Response response) {
        return response.as(Client.class);
    }

    /**
     * This is a funtion to obtain multiple Client classes from a response
     * @param response The response from a request
     * @return A List of Client classes
     */
    public List<Client> getClientsEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Client.class);
    }

    /**
     * This is a function to create a default client using the POST command
     * @return The response of the request
     */
    public Response createDefaultClient() {
        JsonFileReader jsonFile = new JsonFileReader();
        return this.createClient(jsonFile.getClientByJson(Constants.DEFAULT_CLIENT_FILE_PATH));
    }

    /**
     * This is a function to validate the schema of a response with the appropriate client schema
     * @param response The response of the request
     * @param schemaPath The path to the Json containing the schema
     * @return True if schema matches the response, false otherwise
     */
    public boolean validateSchema(Response response, String schemaPath) {
        try {
            response.then()
                    .assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            return true; // Return true if the assertion passes
        } catch (AssertionError e) {
            // Assertion failed, return false
            return false;
        }
    }
}
