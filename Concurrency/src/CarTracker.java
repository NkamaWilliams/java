import java.awt.*;
import java.util.Map;
import java.util.HashMap;
public class CarTracker {
    private Map<String, MutablePoints> locations = new HashMap<>();
    public final int WIDTH = 800;
    public final int HEIGHT = 600;
    public CarTracker(){
        locations.put("Car 001", new MutablePoints(20, 20));
        locations.put("Car 002", new MutablePoints(50, 35));
    }
    public CarTracker(Map<String, MutablePoints> initial){
        locations = deepCopy(initial);
    }

    public synchronized Map<String, MutablePoints> deepCopy(Map<String, MutablePoints> oldLocations){
        Map<String, MutablePoints> result = new HashMap<>();
        for (String name: oldLocations.keySet()){
            MutablePoints point = oldLocations.get(name);
            result.put(name, new MutablePoints(point.x, point.y));
        }
        return result;
    }
    public synchronized void addCar(String name, int x, int y){
        locations.put(name, new MutablePoints(x, y));
    }
    public synchronized void setLocations(String name, int x, int y){
        MutablePoints points = locations.get(name);
        if (points == null){
            throw new IllegalArgumentException("No vehicle with name: " + name);
        }
        points.x = x;
        points.y = y;

    }
    public synchronized Map<String, Point> getLocations(){
        Map<String, Point> result = new HashMap<>();
        for (String name : locations.keySet()){
            MutablePoints point = locations.get(name);
            result.put(name, new Point(point.x, point.y));
        }
        return result;
    }
    public synchronized void randomizeLocations(){
        for (String name: locations.keySet()){
            setLocations(name, (int)(Math.random()*WIDTH), (int)(Math.random()*HEIGHT));
        }
    }

    public static void main(String[] args){
        CarTracker tracker = new CarTracker();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; ++i){
                String name = "Car " + ((int) (Math.random() * 1000));
                tracker.addCar(name, (int)(Math.random()*700), (int)(Math.random()*500));
                tracker.randomizeLocations();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e){e.printStackTrace();}
            }
        });

        Thread t2 = new Thread(() -> {
            while(t1.isAlive()){
                synchronized (tracker){
                    try{
                        Thread.sleep(5);
                    } catch (InterruptedException e){e.printStackTrace();}
                    Map<String, Point> cars = tracker.getLocations();
                    for (String name : cars.keySet()) {
                        Point point = cars.get(name);
                        System.out.println(name + " is at (" + point.getX() + ", " + point.getY() + ")");
                    }
                    System.out.println("\n\n");
                }
            }
        });

        t1.start();
        t2.start();
    }
}

class MutablePoints{
    public int x, y;
    public MutablePoints(){
        x = 0;
        y = 0;
    }
    public MutablePoints(int _x, int _y){
        x = _x;
        y = _y;
    }
}
