package com.satker.elements;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.apache.commons.io.FileUtils.readFileToString;

public class Indexer {
    private String directoryIn;
    // Store the index in memory:
    public static Directory directory = new RAMDirectory();
    // To store an index on disk, use this instead:
    //public static Directory directory = FSDirectory.open("/tmp/testindex");
    public static Analyzer analyzer = new StandardAnalyzer();
    // Сет всевозможных ключей объекта
    public static Set<String> fieldsForSearch = new HashSet<>();
    private Map<String, Object> store = new HashMap<>();

    public Indexer(String directory) {
        this.directoryIn = directory;
    }

    /**
     * Indexed parse JSON file from @method readFile().
     *
     * @throws IOException if there is a low-level I/O error
     */
    public void index() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        Map<Integer, Map<String, String>> mapForIndex = readFile();
        for (int i = 0; i < mapForIndex.size(); i++) {
            Document doc = new Document();
            Map<String, String> interMap = mapForIndex.get(i);
            interMap.keySet().
                    forEach(s ->
                            doc.add(new Field(s,
                                    interMap.get(s),
                                    TextField.TYPE_STORED)));
            iwriter.addDocument(doc);
        }
        iwriter.close();
    }

    /**
     * Parse JSON file.
     *
     * @return return Map of the parse JSON file
     * @throws IOException if there is a low-level I/O error
     */
    private Map<Integer, Map<String, String>> readFile() throws IOException {
        Map<Integer, Map<String, String>> map = new HashMap<>();
        File file = new File(directoryIn);
        JSONTokener tokener = new JSONTokener(readFileToString(file, "utf-8"));
        JSONObject root = new JSONObject(tokener);
        jsonParser(root);
        for (String s : store.keySet()) {
            System.out.println(s + ": " + store.get(s).toString());
        }
        return map;
    }

    private void jsonParser(@NotNull JSONObject jsonObject) {
        for (String s : jsonObject.keySet()) {
            JSONArray jsonArray;
            try {
                // Если объект это массив, то парсим этот массив
                jsonArray = jsonObject.getJSONArray(s);
                jsonArrayParser(jsonArray);
            } catch (JSONException e) {
                // Если объект парсим объект
                jsonObjectParser(jsonObject.getJSONObject(s));
            }
        }
    }

    // Добавляем все в нашу мапу для хранения распарсенного json
    private void jsonObjectParser(@NotNull JSONObject jsonObject) {
        store.putAll(jsonObject.toMap());
    }

    // Если массив то вызывам метод для выбора объекта или массива JSON, замена рекурсии
    private void jsonArrayParser(@NotNull JSONArray jsonArray) {
        for (Object aJsonArray : jsonArray) {
            String next = aJsonArray.toString();
            jsonParser(new JSONObject(next));
        }

    }
}
