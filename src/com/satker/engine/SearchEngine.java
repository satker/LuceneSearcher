package com.satker.engine;

import com.satker.elements.Indexer;
import com.satker.elements.Searcher;

import java.io.IOException;

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

//    public void search(String whatFind, int n, String... keys) throws IOException {
//        Searcher searcher = new Searcher();
//        // Задаем поля для поиска, если надо, по - умолчанию стоят все возможные поля
//        // Текст для поиска
//        List<> searcher.search(whatFind, n);
//        //Iterate through the results:
//        for (ScoreDoc scoreDoc : result) {
//            Document hitDoc = Searcher.isearcher.doc(scoreDoc.doc);
//            for (String key : keys) {
//                System.out.println(hitDoc.get(key));
//            }
//        }
//        Searcher.ireader.close();
//    }

    /**
     * This method print result of search
     *
     * @param whatFind the query of Lucene
     * @param n        number of rows
     */
    public void search(String whatFind, int n) throws IOException {
        whatFind = whatFind + "~";
        Searcher searcher = new Searcher();
        searcher.search(whatFind, n);
        Searcher.ireader.close();
    }

}