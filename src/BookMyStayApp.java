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
            return "Reservation [ID=" + reservationId +
                    ", Guest=" + guestName +
                    ", RoomType=" + roomType + "]";
        }
    }

    // Booking Request Queue (FIFO)
    static class BookingRequestQueue {
        private Queue<Reservation> queue;

        public BookingRequestQueue() {
            queue = new LinkedList<>();
        }

        // Add request (enqueue)
        public void addRequest(Reservation reservation) {
            queue.offer(reservation);
            System.out.println("Request added: " + reservation);
        }

        // View all requests (FIFO order)
        public void viewRequests() {
            if (queue.isEmpty()) {
                System.out.println("No booking requests available.");
                return;
            }

            System.out.println("\nBooking Requests in Queue:");
            for (Reservation r : queue) {
                System.out.println(r);
            }
        }

        // Peek next request (without removing)
        public Reservation peekRequest() {
            return queue.peek();
        }

        // Process next request (dequeue)
        public Reservation processRequest() {
            return queue.poll();
        }
    }

    // Main method
    public static void main(String[] args) {

        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Guests submit booking requests
        requestQueue.addRequest(new Reservation("R101", "Alice", "Deluxe"));
        requestQueue.addRequest(new Reservation("R102", "Bob", "Standard"));
        requestQueue.addRequest(new Reservation("R103", "Charlie", "Suite"));

        // View all requests (FIFO order)
        requestQueue.viewRequests();

        // Peek next request
        System.out.println("\nNext request (to be processed): "
                + requestQueue.peekRequest());

        // Process one request
        System.out.println("\nProcessing request: "
                + requestQueue.processRequest());

        // Remaining queue
        requestQueue.viewRequests();
    }
}