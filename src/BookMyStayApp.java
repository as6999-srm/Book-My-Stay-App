import java.util.*;

// Main class
public class BookMyStayApp {

    // Reservation class
    static class Reservation {
        private String reservationId;
        private String guestName;
        private String roomType;
        private String roomId;
        private boolean isActive;

        public Reservation(String reservationId, String guestName, String roomType, String roomId) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.roomId = roomId;
            this.isActive = true;
        }

        public String getReservationId() {
            return reservationId;
        }

        public String getRoomType() {
            return roomType;
        }

        public String getRoomId() {
            return roomId;
        }

        public boolean isActive() {
            return isActive;
        }

        public void cancel() {
            this.isActive = false;
        }

        @Override
        public String toString() {
            return "Reservation [ID=" + reservationId +
                    ", Guest=" + guestName +
                    ", RoomType=" + roomType +
                    ", RoomID=" + roomId +
                    ", Active=" + isActive + "]";
        }
    }

    // Inventory Manager
    static class InventoryManager {
        private Map<String, Integer> inventory;

        public InventoryManager() {
            inventory = new HashMap<>();
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 2);
            inventory.put("Suite", 1);
        }

        public void decrement(String roomType) {
            inventory.put(roomType, inventory.get(roomType) - 1);
        }

        public void increment(String roomType) {
            inventory.put(roomType, inventory.get(roomType) + 1);
        }

        public void showInventory() {
            System.out.println("\nCurrent Inventory:");
            for (String type : inventory.keySet()) {
                System.out.println(type + ": " + inventory.get(type));
            }
        }
    }

    // Booking History
    static class BookingHistory {
        private Map<String, Reservation> history;

        public BookingHistory() {
            history = new HashMap<>();
        }

        public void addReservation(Reservation r) {
            history.put(r.getReservationId(), r);
        }

        public Reservation getReservation(String id) {
            return history.get(id);
        }

        public void showAll() {
            System.out.println("\nBooking History:");
            for (Reservation r : history.values()) {
                System.out.println(r);
            }
        }
    }

    // Cancellation Service (Rollback using Stack)
    static class CancellationService {
        private InventoryManager inventoryManager;
        private BookingHistory bookingHistory;
        private Stack<String> rollbackStack;

        public CancellationService(InventoryManager inventoryManager, BookingHistory bookingHistory) {
            this.inventoryManager = inventoryManager;
            this.bookingHistory = bookingHistory;
            this.rollbackStack = new Stack<>();
        }

        public void cancelBooking(String reservationId) {

            Reservation reservation = bookingHistory.getReservation(reservationId);

            // Validation
            if (reservation == null) {
                System.out.println("Cancellation failed: Reservation does not exist.");
                return;
            }

            if (!reservation.isActive()) {
                System.out.println("Cancellation failed: Booking already cancelled.");
                return;
            }

            // Step 1: Push room ID to stack (LIFO rollback)
            rollbackStack.push(reservation.getRoomId());

            // Step 2: Restore inventory
            inventoryManager.increment(reservation.getRoomType());

            // Step 3: Mark reservation cancelled
            reservation.cancel();

            System.out.println("Cancellation successful for Reservation ID: " + reservationId);
            System.out.println("Rolled back Room ID: " + rollbackStack.peek());
        }
    }

    // Main method
    public static void main(String[] args) {

        InventoryManager inventoryManager = new InventoryManager();
        BookingHistory bookingHistory = new BookingHistory();
        CancellationService cancellationService =
                new CancellationService(inventoryManager, bookingHistory);

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("R101", "Alice", "Deluxe", "D1");
        Reservation r2 = new Reservation("R102", "Bob", "Suite", "S1");

        // Add to history + reduce inventory
        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);

        inventoryManager.decrement("Deluxe");
        inventoryManager.decrement("Suite");

        inventoryManager.showInventory();
        bookingHistory.showAll();

        // Perform cancellation
        cancellationService.cancelBooking("R102");

        // Try invalid cancellation
        cancellationService.cancelBooking("R102"); // already cancelled
        cancellationService.cancelBooking("R999"); // not exist

        // Final state
        inventoryManager.showInventory();
        bookingHistory.showAll();
    }
}