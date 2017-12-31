package com.satker.elements;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;

import static org.apache.lucene.search.Sort.INDEXORDER;

public class Searcher {
    public static DirectoryReader ireader = null;
    private IndexSearcher isearcher = null;

    /**
     * Search implementation with arbitrary sorting.
     *
     * @param whatFind The query to search for
     * @param n        Return only the top n results
     * @throws IOException if there is a low-level I/O error
     */
    public void search(String whatFind, int n) throws IOException {
        // Now search the index:
        ireader = DirectoryReader.open(Indexer.directory);
        isearcher = new IndexSearcher(ireader);
        for (String field : Indexer.fieldsForSearch) {
            // Parse a simple query that searches for "text":
            QueryParser parser = new QueryParser(field, Indexer.analyzer);
            Query query = null;
            try {
                // What parse
                query = parser.parse(whatFind);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //hits.addAll(Arrays.asList(isearcher.search(query, n, INDEXORDER).scoreDocs));
            show_results(query, n, field);
        }
    }

    /**
     * Search implementation with arbitrary sorting.
     *
     * @param query The query to search for
     * @param n     Return only the top n results
     * @param field Filed in what find text
     * @throws IOException if there is a low-level I/O error
     */
    private void show_results(Query query, int n, String field) throws IOException {
        ScoreDoc[] hits = isearcher.search(query, n, INDEXORDER).scoreDocs;
        for (ScoreDoc hit : hits) {
            Document hitDoc = isearcher.doc(hit.doc);
            System.out.println(field + ": " + hitDoc.get(field));
        }
    }

}
