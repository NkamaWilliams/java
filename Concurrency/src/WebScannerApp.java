import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScannerApp extends JFrame {
    private JTextField queryField;
    private JTextArea resultArea;
    private JButton searchButton;
    private ExecutorService executor;

    public WebScannerApp() {
        setTitle("Web Scanner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JLabel queryLabel = new JLabel("Enter Query:");
        queryField = new JTextField(30);
        searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchListener());

        topPanel.add(queryLabel);
        topPanel.add(queryField);
        topPanel.add(searchButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void search(String query) {
        resultArea.setText(""); // Clear previous results
        executor = Executors.newFixedThreadPool(5);

        String apiKey = "AIzaSyB5oLlt7mIO63taSYmQK5ATzClYnQnZrf0";
        String cx = "d625ccdc0906f4f30";
        String url = "https://www.googleapis.com/customsearch/v1?key=" + apiKey + "&cx=" + cx + "&q=" + query;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                responseBuilder.append("Results");
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }

                reader.close();
                String response = responseBuilder.toString();
                parseSearchResults(response);
            } else {
                handleSearchError("Error: HTTP " + responseCode);
            }
        } catch (IOException e) {
            handleSearchError("Error: " + e.getMessage());
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void parseSearchResults(String response) {
        Map<String, String> results = new HashMap<>();
        Pattern pattern = Pattern.compile("\"link\": \"(.*?)\"");
        Matcher matcher = pattern.matcher(response);

        while (matcher.find()) {
            String link = matcher.group(1);
            executor.execute(new WebPageScanner(link));
        }
    }

    private void appendResult(String result) {
        SwingUtilities.invokeLater(() -> {
            resultArea.append(result);
            resultArea.append("\n");
        });
    }

    private void handleSearchError(String message) {
        SwingUtilities.invokeLater(() -> {
            resultArea.setText(message);
        });
    }

    private class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String query = queryField.getText();
            if (!query.isEmpty()) {
                search(query);
            } else {
                JOptionPane.showMessageDialog(WebScannerApp.this, "Please enter a search query.");
            }
        }
    }

    private class WebPageScanner implements Runnable {
        private String url;

        public WebPageScanner(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder pageContentBuilder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        pageContentBuilder.append(line);
                    }

                    reader.close();
                    String pageContent = pageContentBuilder.toString();
                    // Perform text processing and analysis here
                    // For simplicity, just print the URL
                    appendResult(url);
                } else {
                    appendResult("Error: HTTP " + responseCode + " - " + url);
                }
            } catch (IOException e) {
                appendResult("Error: " + e.getMessage() + " - " + url);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WebScannerApp::new);
    }
}
