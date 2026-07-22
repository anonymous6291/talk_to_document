package talktodocuments.talk_to_documents;

import org.springframework.stereotype.Service;
import talktodocuments.talk_to_documents.database.embedding.QdrantDatabase;

import java.time.Duration;
import java.util.List;

@Service
public class CleanupService {
    private static final boolean RUN_ON_STARTUP = true;
    private static final Duration CLEAN_UP_DELAY = Duration.ofMinutes(5);
    private final QdrantDatabase qdrantDatabase;

    public CleanupService(QdrantDatabase qdrantDatabase) throws Exception {
        this.qdrantDatabase = qdrantDatabase;
        List<String> collections = qdrantDatabase.getAllCollections();
        for (String collectionName : collections) {
            String info = qdrantDatabase.getCollectionInfoAsJson(collectionName);
            IO.println(collectionName + ":");
            IO.println(info);
        }
    }

    private void cleanUP() {
    }
}
