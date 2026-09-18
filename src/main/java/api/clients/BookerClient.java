package api.clients;

import api.models.booker.AuthRequest;
import api.models.booker.AuthResponse;
import api.models.booker.Booking;
import api.models.booker.BookingResponse;
import api.specs.SpecFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookerClient {

    @Step("Authenticate user: {username}")
    public String authenticate(String username, String password) {
        AuthRequest authRequest = AuthRequest.builder()
                .username(username)
                .password(password)
                .build();

        Response response = given()
                .spec(SpecFactory.getBookerRequestSpec())
                .body(authRequest)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract().response();

        return response.as(AuthResponse.class).getToken();
    }

    @Step("Create a new booking")
    public Response createBookingRaw(Booking booking) {
        return given()
                .spec(SpecFactory.getBookerRequestSpec())
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .extract().response();
    }

    @Step("Create a new booking and extract response object")
    public BookingResponse createBooking(Booking booking) {
        return createBookingRaw(booking)
                .then()
                .statusCode(200)
                .extract().as(BookingResponse.class);
    }

    @Step("Get booking raw response by ID: {id}")
    public Response getBookingRaw(int id) {
        return given()
                .spec(SpecFactory.getBookerRequestSpec())
                .pathParam("id", id)
                .when()
                .get("/booking/{id}")
                .then()
                .extract().response();
    }

    @Step("Get booking by ID: {id}")
    public Booking getBooking(int id) {
        return getBookingRaw(id)
                .then()
                .statusCode(200)
                .extract().as(Booking.class);
    }

    @Step("Update booking by ID: {id}")
    public Response updateBooking(int id, Booking booking, String token) {
        return given()
                .spec(token != null ? SpecFactory.getBookerAuthRequestSpec(token) : SpecFactory.getBookerRequestSpec())
                .pathParam("id", id)
                .body(booking)
                .when()
                .put("/booking/{id}")
                .then()
                .extract().response();
    }

    @Step("Delete booking by ID: {id}")
    public Response deleteBooking(int id, String token) {
        return given()
                .spec(token != null ? SpecFactory.getBookerAuthRequestSpec(token) : SpecFactory.getBookerRequestSpec())
                .pathParam("id", id)
                .when()
                .delete("/booking/{id}")
                .then()
                .extract().response();
    }
}
