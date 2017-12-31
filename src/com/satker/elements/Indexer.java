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
    // Set all different keys
    public static Set<String> fieldsForSearch = new HashSet<>();
    // Store parse JSON
    private Map<String, Object> mapForIndex = new HashMap<>();
    // Creates and maintains an index
    private IndexWriter iwriter = null;
    // Store docs for add in indexed field
    private Document doc = null;
    // Store blocks uniques keys
    private ArrayList<String> addInFields = new ArrayList<>();

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
        iwriter = new IndexWriter(directory, config);
        readFile();
        for (String s : mapForIndex.keySet()) {
            Object o = mapForIndex.get(s);
            filedsBuilder(s, o);
        }
        iwriter.close();
    }


    /**
     * Layout JSON on fields
     *
     * @param key Key for field
     * @param obj Possible field
     */
    private void filedsBuilder(String key, Object obj) throws IOException {
        // if obj list with many map
        if (obj instanceof ArrayList) {
            ArrayList<Object> list = (ArrayList<Object>) obj;
            for (Object object : list) {
                filedsBuilder(key, object);
            }
        }
        // If obj map with many key:value
        if (obj instanceof Map) {
            Map<String, Object> interMap = (Map<String, Object>) obj;
            for (String s : interMap.keySet()) {
                filedsBuilder(s, interMap.get(s));
            }
        }
        // if obj single key:value
        else if (!(obj instanceof ArrayList)) {
            if (obj != null) {
                String str = obj.toString();
                // if field is present, then write another doc
                if (addInFields.contains(key)) {
                    fieldsForSearch.addAll(addInFields);
                    addInFields.clear();
                    iwriter.addDocument(doc);
                    doc = new Document();
                    doc.add(new Field(key, str, TextField.TYPE_STORED));
                    addInFields.add(key);
                }
                // if field don't present, continue write define doc
                else {
                    if (doc == null) {
                        doc = new Document();
                    }
                    doc.add(new Field(key, str, TextField.TYPE_STORED));
                    addInFields.add(key);
                }
            }
        }
    }

    /**
     * Parse JSON file.
     *
     * @throws IOException if there is a low-level I/O error
     */
    private void readFile() throws IOException {
        File file = new File(directoryIn);
        JSONTokener tokener = new JSONTokener(readFileToString(file, "utf-8"));
        JSONObject root = new JSONObject(tokener);
        fieldsForSearch.addAll(root.keySet());
        mapForIndex.putAll(root.toMap());
    }
}
