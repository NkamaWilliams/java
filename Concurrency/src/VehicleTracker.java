import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

// MutablePoint class representing (x, y) coordinates
class MutablePoint {
    public int x, y;
    public MutablePoint() {
        x = 0;
        y = 0;
    }
    public MutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

// VehicleTracker class encapsulating the identity and locations of vehicles
public class VehicleTracker {
    private Map<String, MutablePoint> locations;

    public VehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    // Method to return a deep copy of the locations map
    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> locations) {
        Map<String, MutablePoint> result = new HashMap<>();
        for (String id : locations.keySet()) {
            MutablePoint point = locations.get(id);
            result.put(id, new MutablePoint(point.x, point.y));
        }
        return result;
    }

    // Method to return a snapshot of the vehicle locations
    public synchronized Map<String, Point> getLocations() {
        Map<String, Point> result = new HashMap<>();
        for (String id : locations.keySet()) {
            MutablePoint point = locations.get(id);
            result.put(id, new Point(point.x, point.y));
        }
        return result;
    }

    public synchronized void randomizeLocations(){
        for (String id: locations.keySet()){
            setLocation(id, (int) (Math.random() * 800), (int) (Math.random() * 600));
        }
    }

    // Method to update the location of a vehicle
    public synchronized void setLocation(String vehicleId, int x, int y) {
        MutablePoint point = locations.get(vehicleId);
        if (point == null) {
            throw new IllegalArgumentException("Invalid vehicle ID: " + vehicleId);
        }
        point.x = x;
        point.y = y;
    }

    public static void main(String[] args) {
        // Sample usage
        Map<String, MutablePoint> initialLocations = new HashMap<>();
        initialLocations.put("taxi1", new MutablePoint(0, 0));
        initialLocations.put("taxi2", new MutablePoint(10, 10));

        VehicleTracker tracker = new VehicleTracker(initialLocations);

        // Updater thread simulating GPS updates
        new Thread(() -> {
            // Simulate GPS updates
            tracker.setLocation("taxi1", 5, 5);
            tracker.setLocation("taxi2", 15, 15);
            tracker.randomizeLocations();
        }).start();

        // View thread rendering vehicle locations
        new Thread(() -> {
            // Simulate rendering
            Map<String, Point> locations = tracker.getLocations();
            for (String id : locations.keySet()) {
                Point point = locations.get(id);
                System.out.println("Vehicle " + id + " at (" + point.x + ", " + point.y + ")");
            }
        }).start();
    }
}
