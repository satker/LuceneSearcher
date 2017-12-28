import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.apache.lucene.search.Sort.INDEXORDER;

public class Searcher {
    private Object[] fields = Indexer.fieldsForSearch.toArray();
    public DirectoryReader ireader = null;
    public IndexSearcher isearcher = null;

    Searcher(Object[] fields){
        this.fields = fields;
    }

    Searcher(){

    }
    /**
     * Search implementation with arbitrary sorting.
     * @param whatFind The query to search for
     * @param n Return only the top n results
     * @return return 
     * @throws IOException if there is a low-level I/O error
     */
    public List<ScoreDoc> search(String whatFind, int n) throws IOException {
        // Now search the index:
        ireader = DirectoryReader.open(Indexer.directory);
        isearcher = new IndexSearcher(ireader);
        List<ScoreDoc> hits = new ArrayList<>();
        for (Object field : fields) {
            String strSearch = String.valueOf(field);
            // Parse a simple query that searches for "text":
            QueryParser parser = new QueryParser(strSearch, Indexer.analyzer);
            Query query = null;
            try {
                // What parse
                query = parser.parse(whatFind);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            hits.addAll(Arrays.asList(isearcher.search(query, n, INDEXORDER).scoreDocs));
            // Iterate through the results:

        }
        return hits;
    }

}
