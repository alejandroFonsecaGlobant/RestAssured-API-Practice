package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.models.Resource;
import com.testing.api.requests.ClientRequest;
import com.testing.api.requests.ResourceRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.docstring.DocString;
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

public class ResourceSteps {

    private static final Logger logger = LogManager.getLogger(ClientSteps.class);

    private final ResourceRequest resourceRequest = new ResourceRequest();
    private Response response;
    private Resource resource;


    @Given("there are 5 registered resources in the system")
    public void thereAre5RegisteredResourcesInTheSystem() {
        response = resourceRequest.getResources();
        logger.info(response.jsonPath().prettify());
        Assert.assertEquals(200, response.statusCode());

        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        while (resourceList.size() < 5) {
            response = resourceRequest.createDefaultResource();
            logger.info(response.statusCode());
            Assert.assertEquals(201, response.statusCode());
        }
    }

    @When("I send a GET request to view all the resources")
    public void iSendAGETRequestToViewAllTheResources() {
        response = resourceRequest.getResources();
    }

    @And("validates the response with resource list JSON schema")
    public void validatesTheResponseWithResourceListJSONSchema() {
        System.out.println(response.toString());
        String path = "schemas/resourceListSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Resource list schema validated with object");
    }

    @And("I retrieve the details of the latest resource")
    public void iRetrieveTheDetailsOfTheLastResource() {
        response = resourceRequest.getResources();
        List<Resource> resources = resourceRequest.getResourcesEntity(response);
        resource = resources.get(resources.size() - 1);
        logger.info("Latest resource: " + resource);
    }

    @When("I send a PUT request to update the latest resource")
    public void iSendAPUTRequestToUpdateTheLatestResource(String jsonResource) {
        logger.info(resourceRequest.getResourceEntity(jsonResource).toString());
        response = resourceRequest.updateResource(resourceRequest.getResourceEntity(jsonResource),Integer.parseInt(resource.getId()));
        logger.info("Updated resource: " + resourceRequest.getResourceEntity(response));
    }

    @Then("the resource response should have a status code of {int}")
    public void the_resource_response_should_have_a_status_code_of(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @And("validates the response with the resource JSON schema")
    public void validatesTheResponseWithTheResourceJSONSchema() {
        System.out.println(response.toString());
        String path = "schemas/resourceSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Resource schema validated with object");
    }

    @Then("the response should have the following details:")
    public void theResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        Map<String, String> resourceDataMap = expectedData.asMaps().get(0);
        resource = Resource.builder().name(resourceDataMap.get("Name"))
                .trademark(resourceDataMap.get("Trademark"))
                .stock(Integer.parseInt(resourceDataMap.get("Stock")))
                .price(Float.parseFloat(resourceDataMap.get("Price")))
                .description(resourceDataMap.get("Description"))
                .tags(resourceDataMap.get("Tags"))
                .isActive(Boolean.parseBoolean(resourceDataMap.get("Is_active")))
                .build();
        logger.info("Expected resource:" + resource);

        Resource updatedResource = resourceRequest.getResourceEntity(response);
        updatedResource.setId(null);
        Assert.assertEquals(resource, updatedResource);
    }
}
