import com.sun.org.apache.xpath.internal.operations.Bool;
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

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.apache.commons.io.FileUtils.readFileToString;

class Indexer {
    private String directoryIn;
    // Store the index in memory:
    public static Directory directory = new RAMDirectory();
    // To store an index on disk, use this instead:
    //public static Directory directory = FSDirectory.open("/tmp/testindex");
    static Analyzer analyzer = new StandardAnalyzer();
    // Сет всевозможных ключей объекта
    static Set<String> fieldsForSearch = new HashSet<>();

    Indexer(String directory) {
        this.directoryIn = directory;
    }

    // индексация файла
    void index() throws IOException {
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

    private Map<Integer, Map<String, String>> readFile() throws IOException {
        Map<Integer, Map<String, String>> map = new HashMap<>();
        File file = new File(directoryIn);
        String[] content = readFileToString(file, "utf-8").split("\\{");
        int count = 0;
        for (String s : content) {
            if (!s.equals("")) {
                JSONObject tomJsonObject = new JSONObject("{" + s + "}");
                // Сет ключей объекта
                Set<String> keysObjectJson = tomJsonObject.keySet();
                Map<String, String> interMap = new HashMap<>();
                // Добавление ключей в всевозможные значения объектов
                fieldsForSearch.addAll(keysObjectJson);
                for (String key : keysObjectJson) {
                    interMap.put(key, String.valueOf(tomJsonObject.get(key)));
                }
                map.put(count, interMap);
                count++;
            }
        }
        return map;
    }
}
