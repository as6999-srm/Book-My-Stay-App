import java.util.*;

// Main class (as you requested)
public class BookMyStayApp {

    // Reservation class representing a booking request
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }
//okay
        @Override
        public String toString() {
            return "Reservation [Guest=" + guestName + ", RoomType=" + roomType + "]";
        }
    }

    // Booking Request Queue (FIFO)
    static class BookingRequestQueue {
        private Queue<Reservation> queue;

        public BookingRequestQueue() {
            queue = new LinkedList<>();
        }

        // Add booking request
        public void addRequest(Reservation reservation) {
            queue.offer(reservation);
            System.out.println("Request added: " + reservation);
        }

        // View all requests in order
        public void viewRequests() {
            if (queue.isEmpty()) {
                System.out.println("No booking requests in queue.");
                return;
            }

            System.out.println("\nBooking Requests in Queue (FIFO Order):");
            for (Reservation r : queue) {
                System.out.println(r);
            }
        }

        // Get next request (without removing)
        public Reservation peekNextRequest() {
            return queue.peek();
        }

        // Process next request (remove from queue)
        public Reservation processNextRequest() {
            return queue.poll();
        }
    }

    // Main method
    public static void main(String[] args) {

        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Simulating booking requests
        Reservation r1 = new Reservation("Alice", "Deluxe");
        Reservation r2 = new Reservation("Bob", "Standard");
        Reservation r3 = new Reservation("Charlie", "Suite");

        // Adding requests (FIFO order)
        requestQueue.addRequest(r1);
        requestQueue.addRequest(r2);
        requestQueue.addRequest(r3);

        // Viewing queue
        requestQueue.viewRequests();

        // Peek next request
        System.out.println("\nNext request to process: " + requestQueue.peekNextRequest());

        // Process one request
        System.out.println("\nProcessing request: " + requestQueue.processNextRequest());

        // View remaining queue
        requestQueue.viewRequests();
    }
}