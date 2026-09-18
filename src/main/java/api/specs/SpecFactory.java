package api.specs;

import config.Configuration;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class SpecFactory {

    private SpecFactory() {
        // Prevent instantiation
    }

    public static RequestSpecification getBookerRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(Configuration.get().bookerBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept("application/json")
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification getBookerAuthRequestSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBookerRequestSpec())
                .addCookie("token", token)
                .build();
    }

    public static RequestSpecification getGraphQLRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(Configuration.get().graphqlBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept("application/json")
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }
}
