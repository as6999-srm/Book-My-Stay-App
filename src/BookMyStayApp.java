import java.io.*;
import java.util.*;

// Main class
public class BookMyStayApp {

    // Reservation class (Serializable)
    static class Reservation implements Serializable {
        private static final long serialVersionUID = 1L;

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

    // System State (Serializable container)
    static class SystemState implements Serializable {
        private static final long serialVersionUID = 1L;

        Map<String, Integer> inventory;
        List<Reservation> bookingHistory;

        public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
            this.inventory = inventory;
            this.bookingHistory = bookingHistory;
        }
    }

    // Persistence Service
    static class PersistenceService {
        private static final String FILE_NAME = "system_state.dat";

        // Save state to file
        public void save(SystemState state) {
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

                oos.writeObject(state);
                System.out.println("System state saved successfully.");

            } catch (IOException e) {
                System.out.println("Error saving state: " + e.getMessage());
            }
        }

        // Load state from file
        public SystemState load() {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(FILE_NAME))) {

                SystemState state = (SystemState) ois.readObject();
                System.out.println("System state loaded successfully.");
                return state;

            } catch (FileNotFoundException e) {
                System.out.println("No saved state found. Starting fresh.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading state: " + e.getMessage());
            }

            return null; // safe fallback
        }
    }

    // Main method
    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();

        // Try loading previous state
        SystemState state = persistenceService.load();

        Map<String, Integer> inventory;
        List<Reservation> bookingHistory;

        if (state != null) {
            // Restore state
            inventory = state.inventory;
            bookingHistory = state.bookingHistory;
        } else {
            // Fresh start
            inventory = new HashMap<>();
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 2);
            inventory.put("Suite", 1);

            bookingHistory = new ArrayList<>();
        }

        // Simulate new booking
        Reservation r1 = new Reservation("R" + (bookingHistory.size() + 1),
                "Guest" + (bookingHistory.size() + 1),
                "Deluxe");

        // Update system state
        if (inventory.get(r1.getRoomType()) > 0) {
            inventory.put(r1.getRoomType(),
                    inventory.get(r1.getRoomType()) - 1);

            bookingHistory.add(r1);

            System.out.println("Booking confirmed: " + r1);
        } else {
            System.out.println("No rooms available.");
        }

        // Display current state
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }

        System.out.println("\nBooking History:");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }

        // Save state before shutdown
        SystemState newState = new SystemState(inventory, bookingHistory);
        persistenceService.save(newState);
    }
}