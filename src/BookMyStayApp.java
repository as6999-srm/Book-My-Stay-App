import java.util.*;

// Main class
public class BookMyStayApp {

    // Reservation (Booking Request)
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
            return "[ID=" + reservationId + ", Guest=" + guestName + ", Room=" + roomType + "]";
        }
    }

    // Shared Booking Queue (Thread-safe access)
    static class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public synchronized void addRequest(Reservation r) {
            queue.offer(r);
            System.out.println(Thread.currentThread().getName() +
                    " added request: " + r);
        }

        public synchronized Reservation getNextRequest() {
            return queue.poll();
        }
    }

    // Inventory Manager (CRITICAL SECTION)
    static class InventoryManager {
        private Map<String, Integer> inventory = new HashMap<>();

        public InventoryManager() {
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 1);
            inventory.put("Suite", 1);
        }

        // synchronized ensures only ONE thread updates inventory at a time
        public synchronized boolean allocateRoom(String roomType) {
            int available = inventory.getOrDefault(roomType, 0);

            if (available > 0) {
                inventory.put(roomType, available - 1);
                return true;
            }
            return false;
        }

        public void showInventory() {
            System.out.println("\nFinal Inventory:");
            for (String type : inventory.keySet()) {
                System.out.println(type + ": " + inventory.get(type));
            }
        }
    }

    // Worker Thread (Concurrent Booking Processor)
    static class BookingWorker extends Thread {
        private BookingQueue queue;
        private InventoryManager inventory;

        public BookingWorker(BookingQueue queue, InventoryManager inventory, String name) {
            super(name);
            this.queue = queue;
            this.inventory = inventory;
        }

        @Override
        public void run() {
            while (true) {
                Reservation r;

                // synchronized retrieval
                synchronized (queue) {
                    r = queue.getNextRequest();
                }

                if (r == null) break;

                // CRITICAL SECTION (inventory update)
                boolean success = inventory.allocateRoom(r.getRoomType());

                if (success) {
                    System.out.println(getName() +
                            " SUCCESS booking: " + r);
                } else {
                    System.out.println(getName() +
                            " FAILED (no rooms): " + r);
                }

                try {
                    Thread.sleep(100); // simulate delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        InventoryManager inventory = new InventoryManager();

        // Simulate multiple booking requests
        queue.addRequest(new Reservation("R1", "Alice", "Deluxe"));
        queue.addRequest(new Reservation("R2", "Bob", "Deluxe"));
        queue.addRequest(new Reservation("R3", "Charlie", "Suite"));
        queue.addRequest(new Reservation("R4", "David", "Suite"));
        queue.addRequest(new Reservation("R5", "Eve", "Standard"));
        queue.addRequest(new Reservation("R6", "Frank", "Standard"));

        // Create multiple threads (simulating multiple users)
        BookingWorker t1 = new BookingWorker(queue, inventory, "Thread-1");
        BookingWorker t2 = new BookingWorker(queue, inventory, "Thread-2");
        BookingWorker t3 = new BookingWorker(queue, inventory, "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final inventory state
        inventory.showInventory();
    }
}