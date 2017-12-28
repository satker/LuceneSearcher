import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.List;

public class SearchEngine {
    /**
     * This method indexed json file
     *
     * @param directory the name of the directory for search
     */
    public static void indexer(String directory) throws IOException {
        Indexer ind = new Indexer(directory);
        ind.index();
    }

    /**
     * This method print result of search
     *
     * @param whatFind the query of Lucene
     */
    public void search(String whatFind, int n) throws IOException {
        Searcher searcher = new Searcher();
        // Задаем поля для поиска, если надо, по - умолчанию стоят все возможные поля
        // Текст для поиска
        List<ScoreDoc> result = searcher.search(whatFind, n);
        // Iterate through the results:
        for (ScoreDoc scoreDoc : result) {
            Document hitDoc = searcher.isearcher.doc(scoreDoc.doc);
            System.out.println(hitDoc.get("date"));
        }
        searcher.ireader.close();
    }
}