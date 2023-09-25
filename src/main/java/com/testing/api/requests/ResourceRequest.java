package com.testing.api.requests;

import com.google.gson.Gson;
import com.testing.api.models.Client;
import com.testing.api.models.Resource;
import com.testing.api.utils.Constants;
import com.testing.api.utils.JsonFileReader;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResourceRequest extends BaseRequest{

    private String endpoint;

    /**
     * This is a function to get all the resources
     * @return Response of the request
     */
    public Response getResources() {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * This is a function to create a new Resource
     * @param resource Resource to create
     * @return Response of the request
     */
    public Response createResource(Resource resource) {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestPost(endpoint, createBaseHeaders(), resource);
    }

    /**
     * This is a function to update an existing Resource
     * @param resource Resource with new data
     * @param resourceId ID of the resource to be updated
     * @return Response of the request
     */
    public Response updateResource(Resource resource, int resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestPut(endpoint, createBaseHeaders(), resource);
    }

    /**
     * This is a function to obtain a Resource class from a response
     * @param response The response from a request
     * @return A Resource class
     */
    public Resource getResourceEntity(@NotNull Response response) {
        return response.as(Resource.class);
    }
    public List<Resource> getResourcesEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Resource.class);
    }

    /**
     * This is a function to create a default Resource using the POST command
     * @return The response of the request
     */
    public Response createDefaultResource() {
        JsonFileReader jsonFile = new JsonFileReader();
        return this.createResource(jsonFile.getResourceByJson(Constants.DEFAULT_RESOURCE_FILE_PATH));
    }

    /**
     * This is a function to obtain a Resource class from a json
     * @param resourceJson The json to be converted
     * @return A Resource class
     */
    public Resource getResourceEntity(String resourceJson) {
        Gson gson = new Gson();
        return gson.fromJson(resourceJson, Resource.class);
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
