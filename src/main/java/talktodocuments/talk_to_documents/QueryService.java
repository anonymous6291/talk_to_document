package talktodocuments.talk_to_documents;

import org.springframework.stereotype.Service;
import talktodocuments.talk_to_documents.database.embedding.Chunk;
import talktodocuments.talk_to_documents.database.embedding.QdrantDatabase;
import talktodocuments.talk_to_documents.models.conversational.Conversation;
import talktodocuments.talk_to_documents.models.embedding.Embedder;

import java.util.List;

@Service
public class QueryService {
    private final Embedder embedder;
    private final QdrantDatabase qdrantDatabase;
    private final Conversation conversation;

    public QueryService(Embedder embedder, QdrantDatabase qdrantDatabase, Conversation conversation) {
        this.embedder = embedder;
        this.qdrantDatabase = qdrantDatabase;
        this.conversation = conversation;
    }

    public String query(List<String> allowedDocumentIds, String query, String collectionName) throws Exception {
        List<float[]> embedding = embedder.getEmbeddings(List.of(query));
        List<Chunk> matchingChunks = qdrantDatabase.searchInAll(collectionName, embedding.getFirst(), "documentId", allowedDocumentIds);
        return conversation.sendPrompt(matchingChunks.stream().map(x -> x.payload().text()).toList(), query);
    }
}