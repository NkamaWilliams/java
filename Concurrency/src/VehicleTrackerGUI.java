import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.HashMap;

public class VehicleTrackerGUI extends JFrame {
    private CarTracker tracker;
    private Map<String, Point> vehiclePositions;
    private VehiclePanel panel;

    public VehicleTrackerGUI(CarTracker tracker) {
        this.tracker = tracker;
        this.vehiclePositions = tracker.getLocations();

        setTitle("Vehicle Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new VehiclePanel();
        add(panel, BorderLayout.CENTER);

        JButton updateButton = new JButton("Update Positions");
        JButton newCar = new JButton("Add new car");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateVehiclePositions();
            }
        });
        newCar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewCar();
            }
        });
        add(updateButton, BorderLayout.SOUTH);
        add(newCar, BorderLayout.NORTH);
    }

    class VehiclePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (String id : vehiclePositions.keySet()) {
                Point point = vehiclePositions.get(id);
                int x = (int) point.getX();
                int y = (int) point.getY();
                g.setColor(Color.RED);
                g.fillOval(x, y, 10, 10); // Draw vehicle as a blue circle
                g.setColor(Color.BLUE);
                g.drawString(id, x, y); // Display vehicle ID
            }
        }
    }

    public void updateVehiclePositions() {
        System.out.println("Updating vehicle positions...");
        tracker.randomizeLocations(); // Update positions in the tracker
        vehiclePositions = tracker.getLocations();
        System.out.println("Positions updated.");
        panel.repaint(); // Refresh the GUI
        System.out.println("GUI refreshed.");
    }

    public void addNewCar(){
        String newName = "Car 00" + ((int) (Math.random() * 100));
        tracker.addCar(newName, (int) (Math.random() * 100), (int) (Math.random() * 100));
        vehiclePositions = tracker.getLocations();
        panel.repaint();
    }

    public static void main(String[] args) {
        // Sample usage

        CarTracker tracker = new CarTracker();

        SwingUtilities.invokeLater(() -> {
            VehicleTrackerGUI gui = new VehicleTrackerGUI(tracker);
            gui.setVisible(true);
        });
    }
}
