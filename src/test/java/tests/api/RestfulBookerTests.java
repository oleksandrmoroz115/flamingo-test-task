package tests.api;

import api.clients.BookerClient;
import api.data.BookerDataGenerator;
import api.models.booker.Booking;
import api.models.booker.BookingResponse;
import config.Configuration;
import infrastructure.fixtures.ApiFixtureExtension;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Feature("Restful Booker API")
@ExtendWith(ApiFixtureExtension.class)
public class RestfulBookerTests {

    private String getAuthToken(BookerClient client) {
        String username = Configuration.get().bookerUsername();
        String password = Configuration.get().bookerPassword();
        return client.authenticate(username, password);
    }

    @Test
    @Story("Create Booking")
    @Description("Verify creating a new booking returns 200 and correct payload schema")
    public void createBookingTest(BookerClient client) {
        Booking newBooking = BookerDataGenerator.createDefaultBooking();
        BookingResponse createResponse = client.createBooking(newBooking);
        
        assertThat(createResponse.getBookingid()).isGreaterThan(0);
        assertThat(createResponse.getBooking().getFirstname()).isEqualTo(newBooking.getFirstname());
        assertThat(createResponse.getBooking().getLastname()).isEqualTo(newBooking.getLastname());
    }

    @Test
    @Story("Read Booking")
    @Description("Verify retrieving an existing booking by ID validates the contract")
    public void getBookingByIdTest(BookerClient client) {
        // Create
        Booking newBooking = BookerDataGenerator.createDefaultBooking();
        int bookingId = client.createBooking(newBooking).getBookingid();

        // Read
        Booking fetchedBooking = client.getBooking(bookingId);
        assertThat(fetchedBooking.getFirstname()).isEqualTo(newBooking.getFirstname());
        assertThat(fetchedBooking.getTotalprice()).isEqualTo(newBooking.getTotalprice());
    }

    @Test
    @Story("Update Booking")
    @Description("Verify updating a booking (PUT) modifies the resource correctly")
    public void updateBookingTest(BookerClient client) {
        String authToken = getAuthToken(client);
        
        // Create
        Booking newBooking = BookerDataGenerator.createDefaultBooking();
        int bookingId = client.createBooking(newBooking).getBookingid();

        // Update
        Booking updatedBooking = BookerDataGenerator.createDefaultBooking();
        updatedBooking.setFirstname("UpdatedName");
        updatedBooking.setTotalprice(500);

        Response updateResponseRaw = client.updateBooking(bookingId, updatedBooking, authToken);
        assertThat(updateResponseRaw.statusCode()).isEqualTo(200);

        // Verify Update
        Booking verifiedUpdatedBooking = client.getBooking(bookingId);
        assertThat(verifiedUpdatedBooking.getFirstname()).isEqualTo("UpdatedName");
        assertThat(verifiedUpdatedBooking.getTotalprice()).isEqualTo(500);
    }

    @Test
    @Story("Delete Booking")
    @Description("Verify deleting a booking returns 201 and subsequent GET returns 404")
    public void deleteBookingTest(BookerClient client) {
        String authToken = getAuthToken(client);
        
        // Create
        Booking newBooking = BookerDataGenerator.createDefaultBooking();
        int bookingId = client.createBooking(newBooking).getBookingid();

        // Delete
        Response deleteResponse = client.deleteBooking(bookingId, authToken);
        assertThat(deleteResponse.statusCode()).isEqualTo(201); // Based on RESTful Booker docs

        // Verify 404
        Response getAfterDeleteResponse = client.getBookingRaw(bookingId);
        assertThat(getAfterDeleteResponse.statusCode()).isEqualTo(404);
    }

    @Test
    @Story("Read Booking")
    @Description("Verify GET request for a non-existent booking returns 404")
    public void testGetNonExistentBooking(BookerClient client) {
        Response response = client.getBookingRaw(99999999);
        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    @Story("Update Booking")
    @Description("Verify PUT request without an auth token returns 403 Forbidden")
    public void testUpdateBookingWithoutAuth(BookerClient client) {
        // First, create a valid booking to ensure we have a valid ID
        Booking newBooking = BookerDataGenerator.createDefaultBooking();
        BookingResponse createResponse = client.createBooking(newBooking);
        int bookingId = createResponse.getBookingid();

        // Attempt update without token
        Booking updatedBooking = BookerDataGenerator.createDefaultBooking();
        Response response = client.updateBooking(bookingId, updatedBooking, null);
        
        assertThat(response.statusCode()).isEqualTo(403);
    }

    @Test
    @Story("Delete Booking")
    @Description("Verify DELETE request without an auth token returns 403 Forbidden")
    public void testDeleteBookingWithoutAuth(BookerClient client) {
        // First, create a valid booking to ensure we have a valid ID
        Booking newBooking = BookerDataGenerator.createDefaultBooking();
        BookingResponse createResponse = client.createBooking(newBooking);
        int bookingId = createResponse.getBookingid();

        // Attempt delete without token
        Response response = client.deleteBooking(bookingId, null);
        
        assertThat(response.statusCode()).isEqualTo(403);
    }
}
