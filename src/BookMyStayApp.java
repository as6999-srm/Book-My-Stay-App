import java.util.*;

// Main class
public class BookMyStayApp {

    // Reservation class
    static class Reservation {
        private String reservationId;
        private String guestName;
        private String roomType;

        public Reservation(String reservationId, String guestName, String roomType) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getReservationId() {
            return reservationId;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }

        @Override
        public String toString() {
            return "Reservation [ID=" + reservationId +
                    ", Guest=" + guestName +
                    ", RoomType=" + roomType + "]";
        }
    }

    // Booking History (stores confirmed bookings)
    static class BookingHistory {
        private List<Reservation> history;

        public BookingHistory() {
            history = new ArrayList<>();
        }

        // Add confirmed reservation
        public void addReservation(Reservation reservation) {
            history.add(reservation);
            System.out.println("Added to history: " + reservation);
        }

        // Get all reservations
        public List<Reservation> getAllReservations() {
            return history;
        }
    }

    // Reporting Service
    static class BookingReportService {

        // Display all bookings
        public void showAllBookings(List<Reservation> reservations) {
            if (reservations.isEmpty()) {
                System.out.println("No bookings found.");
                return;
            }

            System.out.println("\n--- Booking History ---");
            for (Reservation r : reservations) {
                System.out.println(r);
            }
        }

        // Generate summary report
        public void generateSummary(List<Reservation> reservations) {
            System.out.println("\n--- Booking Summary Report ---");

            System.out.println("Total Bookings: " + reservations.size());

            // Count by room type
            Map<String, Integer> roomTypeCount = new HashMap<>();

            for (Reservation r : reservations) {
                roomTypeCount.put(
                        r.getRoomType(),
                        roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
                );
            }

            System.out.println("Bookings by Room Type:");
            for (String type : roomTypeCount.keySet()) {
                System.out.println(type + ": " + roomTypeCount.get(type));
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings
        Reservation r1 = new Reservation("R101", "Alice", "Deluxe");
        Reservation r2 = new Reservation("R102", "Bob", "Standard");
        Reservation r3 = new Reservation("R103", "Charlie", "Suite");
        Reservation r4 = new Reservation("R104", "David", "Deluxe");

        // Add to booking history
        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);
        bookingHistory.addReservation(r4);

        // Admin views booking history
        reportService.showAllBookings(bookingHistory.getAllReservations());

        // Admin generates report
        reportService.generateSummary(bookingHistory.getAllReservations());
    }
}