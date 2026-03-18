import java.util.*;

// Main class
public class BookMyStayApp {

    // Reservation class
    static class Reservation {
        private String reservationId;
        private String guestName;

        public Reservation(String reservationId, String guestName) {
            this.reservationId = reservationId;
            this.guestName = guestName;
        }

        public String getReservationId() {
            return reservationId;
        }

        public String getGuestName() {
            return guestName;
        }

        @Override
        public String toString() {
            return "Reservation [ID=" + reservationId + ", Guest=" + guestName + "]";
        }
    }

    // Add-On Service class
    static class Service {
        private String serviceName;
        private double cost;

        public Service(String serviceName, double cost) {
            this.serviceName = serviceName;
            this.cost = cost;
        }

        public String getServiceName() {
            return serviceName;
        }

        public double getCost() {
            return cost;
        }

        @Override
        public String toString() {
            return serviceName + " (₹" + cost + ")";
        }
    }

    // Add-On Service Manager
    static class AddOnServiceManager {
        private Map<String, List<Service>> serviceMap;

        public AddOnServiceManager() {
            serviceMap = new HashMap<>();
        }

        // Add service to reservation
        public void addService(String reservationId, Service service) {
            serviceMap.putIfAbsent(reservationId, new ArrayList<>());
            serviceMap.get(reservationId).add(service);

            System.out.println("Added service " + service + " to Reservation ID: " + reservationId);
        }

        // View services for a reservation
        public void viewServices(String reservationId) {
            List<Service> services = serviceMap.get(reservationId);

            if (services == null || services.isEmpty()) {
                System.out.println("No add-on services for Reservation ID: " + reservationId);
                return;
            }

            System.out.println("\nServices for Reservation ID: " + reservationId);
            for (Service s : services) {
                System.out.println(s);
            }
        }

        // Calculate total cost of services
        public double calculateTotalCost(String reservationId) {
            List<Service> services = serviceMap.get(reservationId);
            double total = 0;

            if (services != null) {
                for (Service s : services) {
                    total += s.getCost();
                }
            }

            return total;
        }
    }

    // Main method
    public static void main(String[] args) {

        // Create reservations
        Reservation r1 = new Reservation("R101", "Alice");
        Reservation r2 = new Reservation("R102", "Bob");

        // Create service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Create services
        Service breakfast = new Service("Breakfast", 500);
        Service spa = new Service("Spa", 1500);
        Service airportPickup = new Service("Airport Pickup", 800);

        // Add services to reservations
        manager.addService(r1.getReservationId(), breakfast);
        manager.addService(r1.getReservationId(), spa);

        manager.addService(r2.getReservationId(), airportPickup);

        // View services
        manager.viewServices(r1.getReservationId());
        manager.viewServices(r2.getReservationId());

        // Calculate total cost
        System.out.println("\nTotal Add-On Cost for " + r1.getReservationId() + ": ₹"
                + manager.calculateTotalCost(r1.getReservationId()));

        System.out.println("Total Add-On Cost for " + r2.getReservationId() + ": ₹"
                + manager.calculateTotalCost(r2.getReservationId()));
    }
}