package org.example.booking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingReportGenerator {
    record Booking(String bookingId, String hotelId, String status) {}
    record Payment(String paymentId, String bookingId, int amount) {}
    record HotelReport(String hotelId, int confirmedBookings, int revenue) {}

    public static List<HotelReport> generateReport(List<Booking> bookings, List<Payment> payments) {
        Map<String, Booking> bookingById = new HashMap<>();
        for (Booking booking : bookings) {
            bookingById.put(booking.bookingId(), booking);
        }

        Map<String, Integer> bookingCountByHotel = new HashMap<>();
        Map<String, Integer> revenueByHotel = new HashMap<>();

        for (Payment payment : payments) {
            Booking booking = bookingById.get(payment.bookingId());
            if (booking == null) continue;
            if (!"confirmed".equalsIgnoreCase(booking.status())) continue;

            bookingCountByHotel.merge(booking.hotelId(), 1, Integer::sum);
            revenueByHotel.merge(booking.hotelId(), payment.amount(), Integer::sum);
        }

        List<HotelReport> result = new ArrayList<>();
        for (String hotelId : bookingCountByHotel.keySet()) {
            result.add(new HotelReport(
                    hotelId,
                    bookingCountByHotel.getOrDefault(hotelId, 0),
                    revenueByHotel.getOrDefault(hotelId, 0)
            ));
        }

        result.sort(Comparator.comparing(HotelReport::hotelId));
        return result;
    }

    public static void main(String[] args) {
        List<Booking> bookings = List.of(
                new Booking("b1", "h1", "confirmed"),
                new Booking("b2", "h2", "confirmed"),
                new Booking("b3", "h1", "confirmed")
        );

        List<Payment> payments = List.of(
                new Payment("p1", "b1", 100),
                new Payment("p2", "b2", 200),
                new Payment("p3", "b3", 150)
        );

        System.out.println(generateReport(bookings, payments));
    }
}
