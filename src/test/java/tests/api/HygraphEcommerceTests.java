package tests.api;

import api.clients.GraphQLClient;
import api.models.graphql.GraphQLRequest;
import infrastructure.fixtures.ApiFixtureExtension;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Feature("GraphQL Ecommerce API")
@ExtendWith(ApiFixtureExtension.class)
public class HygraphEcommerceTests {

    @Test
    @Story("Query Products")
    @Description("Query products with first/limit argument and assert pagination size")
    public void testQueryProductsWithPaginationAndLimit(GraphQLClient client) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("first", 2);

        GraphQLRequest request = GraphQLRequest.builder()
                .operationName("GetProducts")
                .query("query GetProducts($first: Int!) { products(first: $first) { id name } }")
                .variables(variables)
                .build();

        Response response = client.execute(request);
        assertThat(response.statusCode()).isEqualTo(200);

        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> products = jsonPath.getList("data.products");
        
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    @Story("Query Product by ID")
    @Description("Query a single product by ID using GraphQL variables")
    public void testQueryProductByIdUsingVariables(GraphQLClient client) {
        // First, get a valid product ID
        GraphQLRequest setupReq = GraphQLRequest.builder()
                .query("query { products(first: 1) { id } }")
                .build();
        Response setupRes = client.execute(setupReq);
        String productId = setupRes.jsonPath().getString("data.products[0].id");
        assertThat(productId).isNotBlank();

        // Now query by ID using variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", productId);

        GraphQLRequest request = GraphQLRequest.builder()
                .operationName("GetProductById")
                .query("query GetProductById($id: ID!) { product(where: { id: $id }) { id name } }")
                .variables(variables)
                .build();

        Response response = client.execute(request);
        assertThat(response.statusCode()).isEqualTo(200);

        JsonPath jsonPath = response.jsonPath();
        String returnedId = jsonPath.getString("data.product.id");
        String name = jsonPath.getString("data.product.name");

        assertThat(returnedId).isEqualTo(productId);
        assertThat(name).isNotBlank();
    }

    @Test
    @Story("Query Nested Fields and Fragments")
    @Description("Query products using a fragment and associations like categories")
    public void testQueryWithFragmentsAndNestedFields(GraphQLClient client) {
        String query = "fragment ProductFields on Product { id name price } " +
                "query GetProductsWithCategories { products(first: 1) { ...ProductFields categories { id name } } }";

        GraphQLRequest request = GraphQLRequest.builder()
                .operationName("GetProductsWithCategories")
                .query(query)
                .build();

        Response response = client.execute(request);
        assertThat(response.statusCode()).isEqualTo(200);

        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> products = jsonPath.getList("data.products");
        assertThat(products).isNotEmpty();

        Map<String, Object> firstProduct = products.get(0);
        assertThat(firstProduct).containsKey("id");
        assertThat(firstProduct).containsKey("name");
        assertThat(firstProduct).containsKey("price");
        assertThat(firstProduct).containsKey("categories");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> categories = (List<Map<String, Object>>) firstProduct.get("categories");
        assertThat(categories).isNotNull();
    }

    @Test
    @Story("Negative - Non-existent ID")
    @Description("Query a product with a non-existent ID")
    public void testQueryWithNonExistentId(GraphQLClient client) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", "fake_non_existent_id_999");

        GraphQLRequest request = GraphQLRequest.builder()
                .operationName("GetProductById")
                .query("query GetProductById($id: ID!) { product(where: { id: $id }) { id name } }")
                .variables(variables)
                .build();

        Response response = client.execute(request);
        assertThat(response.statusCode()).isEqualTo(200);

        JsonPath jsonPath = response.jsonPath();
        Object product = jsonPath.get("data.product");
        assertThat(product).isNull();
    }

    @Test
    @Story("Negative - Malformed Syntax")
    @Description("Query with broken syntax returns errors array and no data")
    public void testMalformedQuerySyntax(GraphQLClient client) {
        // Missing closing braces
        String badQuery = "query { products(first: 1) { id name ";

        GraphQLRequest request = GraphQLRequest.builder()
                .query(badQuery)
                .build();

        Response response = client.execute(request);
        // GraphQL servers typically return 200 or 400 for syntax errors, we just check status isn't 500
        assertThat(response.statusCode()).isBetween(200, 400);

        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> errors = jsonPath.getList("errors");
        assertThat(errors).isNotEmpty();
        assertThat(errors.get(0).get("message").toString()).isNotBlank();
        
        Object data = jsonPath.get("data");
        assertThat(data).isNull();
    }

    @Test
    @Story("Negative - Non-existent Field")
    @Description("Query requesting a field not in schema returns validation error")
    public void testQueryNonExistentField(GraphQLClient client) {
        String badQuery = "query { products(first: 1) { id fakeFieldXYZ } }";

        GraphQLRequest request = GraphQLRequest.builder()
                .query(badQuery)
                .build();

        Response response = client.execute(request);
        assertThat(response.statusCode()).isBetween(200, 400);

        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> errors = jsonPath.getList("errors");
        assertThat(errors).isNotNull().isNotEmpty();
        assertThat(errors.get(0).get("message").toString()).isNotBlank();
    }
}
