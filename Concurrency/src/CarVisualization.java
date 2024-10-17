import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class CarVisualization extends JFrame {
    private CarTracker tracker;
    private JPanel drawingPanel;

    public CarVisualization() {
        tracker = new CarTracker();

        setTitle("Car Visualization");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel controlPanel = new JPanel();
        JButton addButton = new JButton("Add Car");
        addButton.addActionListener(new AddCarListener());
        JButton randomizeButton = new JButton("Update Locations");
        randomizeButton.addActionListener(new RandomizeListener());
        controlPanel.add(addButton);
        controlPanel.add(randomizeButton);

        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Map<String, Point> locations = tracker.getLocations();
                for (String name : locations.keySet()) {
                    Point point = locations.get(name);
                    g.setColor(Color.BLUE);
                    g.fillOval(point.x, point.y, 15, 15);
                    g.setColor(Color.BLACK);
                    g.drawString(name, point.x - 5, point.y - 5);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1000, 800);
            }
        };

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(drawingPanel, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private class AddCarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = "Car " + ((int) (Math.random() * 1000));
            tracker.addCar(name, (int) (Math.random() * 700), (int) (Math.random() * 500));
            drawingPanel.repaint();
        }
    }

    private class RandomizeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tracker.randomizeLocations();
            drawingPanel.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarVisualization());
    }
}
