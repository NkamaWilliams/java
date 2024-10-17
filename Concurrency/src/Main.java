import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome! What do you want to search for?");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        String searchURL = "https://www.google.com/search?q=" + query + "&num=20";

        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla").get();
        Elements searchResults = doc.select("div.g");

        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<SearchResult> resultList = new ArrayList<>();

        for (Element result : searchResults) {
            Element link = result.select("a[href]").first();
            String title = result.select("h3").text();
            String url = link.attr("href");

            if (!title.isEmpty() && !url.isEmpty()) {
                SearchResult searchResult = new SearchResult(title, url);
                resultList.add(searchResult);
                executor.execute(new SearchTask(searchResult));
            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all threads to finish
        }

        // Display results
        System.out.println("Search Results:");
        int rank = 1;
        for (SearchResult result : resultList) {
            System.out.println(rank++ + ". " + result.getTitle() + " - " + result.getUrl());
        }
    }
}

class SearchTask implements Runnable {
    private SearchResult searchResult;

    public SearchTask(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    @Override
    public void run() {
        // Perform any additional processing for each search result if needed
        // Here you can fetch additional information or perform other tasks related to each search result
    }
}

class SearchResult {
    private String title;
    private String url;

    public SearchResult(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
