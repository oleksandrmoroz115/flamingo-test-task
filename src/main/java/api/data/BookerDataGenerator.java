package api.data;

import api.models.booker.Booking;
import api.models.booker.BookingDates;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class BookerDataGenerator {

    private BookerDataGenerator() {
        // Prevent instantiation
    }

    public static Booking createDefaultBooking() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        BookingDates dates = BookingDates.builder()
                .checkin(today.format(formatter))
                .checkout(nextWeek.format(formatter))
                .build();

        return Booking.builder()
                .firstname("John" + UUID.randomUUID().toString().substring(0, 4))
                .lastname("Doe" + UUID.randomUUID().toString().substring(0, 4))
                .totalprice(150)
                .depositpaid(true)
                .bookingdates(dates)
                .additionalneeds("Breakfast")
                .build();
    }
}
