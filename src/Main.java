import com.satker.engine.SearchEngine;

import java.io.IOException;

public class Main { public static void main(String[] args) {
    SearchEngine main = new SearchEngine();
    try {
        SearchEngine.indexer("D:\\citylots.json");
        main.search("Feature1221", 1,"type");
        main.search("2571", 1, "type");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
