package api.clients;

import api.models.graphql.GraphQLRequest;
import api.specs.SpecFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GraphQLClient {

    @Step("Execute GraphQL Query: {request.operationName}")
    public Response execute(GraphQLRequest request) {
        return given()
                .spec(SpecFactory.getGraphQLRequestSpec())
                .body(request)
                .when()
                .post()
                .then()
                .extract().response();
    }
}
