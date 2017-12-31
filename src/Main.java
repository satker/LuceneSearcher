import com.satker.engine.SearchEngine;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SearchEngine main = new SearchEngine();
        try {
            // Create index of our file
            SearchEngine.indexer("D:\\news.json");
            // Lets search, visualize top n results
            main.search("Погодные", 10);
            main.search("ццц", 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
