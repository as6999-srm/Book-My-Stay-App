import java.util.*;

// Main class
public class BookMyStayApp {

    // Custom Exception for Invalid Booking
    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }

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

    // Inventory Manager (for validation)
    static class InventoryManager {
        private Map<String, Integer> roomInventory;

        public InventoryManager() {
            roomInventory = new HashMap<>();

            // Initial inventory
            roomInventory.put("Standard", 2);
            roomInventory.put("Deluxe", 2);
            roomInventory.put("Suite", 1);
        }

        // Validate room type
        public void validateRoomType(String roomType) throws InvalidBookingException {
            if (!roomInventory.containsKey(roomType)) {
                throw new InvalidBookingException("Invalid room type: " + roomType);
            }
        }

        // Validate availability
        public void validateAvailability(String roomType) throws InvalidBookingException {
            int available = roomInventory.get(roomType);

            if (available <= 0) {
                throw new InvalidBookingException("No rooms available for type: " + roomType);
            }
        }

        // Allocate room (safe update)
        public void allocateRoom(String roomType) throws InvalidBookingException {
            validateRoomType(roomType);
            validateAvailability(roomType);

            roomInventory.put(roomType, roomInventory.get(roomType) - 1);
        }

        public void showInventory() {
            System.out.println("\nCurrent Inventory:");
            for (String type : roomInventory.keySet()) {
                System.out.println(type + ": " + roomInventory.get(type));
            }
        }
    }

    // Validator + Booking Processor
    static class BookingProcessor {
        private InventoryManager inventoryManager;

        public BookingProcessor(InventoryManager inventoryManager) {
            this.inventoryManager = inventoryManager;
        }

        public void processBooking(Reservation reservation) {
            try {
                // Fail-fast validation
                if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
                    throw new InvalidBookingException("Guest name cannot be empty.");
                }

                inventoryManager.allocateRoom(reservation.getRoomType());

                System.out.println("Booking successful: " + reservation);

            } catch (InvalidBookingException e) {
                // Graceful failure
                System.out.println("Booking failed: " + e.getMessage());
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        InventoryManager inventoryManager = new InventoryManager();
        BookingProcessor processor = new BookingProcessor(inventoryManager);

        // Valid booking
        Reservation r1 = new Reservation("R101", "Alice", "Deluxe");

        // Invalid room type
        Reservation r2 = new Reservation("R102", "Bob", "Premium");

        // Empty guest name
        Reservation r3 = new Reservation("R103", "", "Standard");

        // Exceeding availability
        Reservation r4 = new Reservation("R104", "Charlie", "Suite");
        Reservation r5 = new Reservation("R105", "David", "Suite"); // should fail

        // Process bookings
        processor.processBooking(r1);
        processor.processBooking(r2);
        processor.processBooking(r3);
        processor.processBooking(r4);
        processor.processBooking(r5);

        // Show final inventory
        inventoryManager.showInventory();
    }
}