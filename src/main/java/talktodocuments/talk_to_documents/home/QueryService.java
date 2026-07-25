package talktodocuments.talk_to_documents.home;

import org.springframework.stereotype.Service;
import talktodocuments.talk_to_documents.database.embedding.Chunk;
import talktodocuments.talk_to_documents.database.embedding.QdrantDatabase;
import talktodocuments.talk_to_documents.models.conversational.Conversation;
import talktodocuments.talk_to_documents.models.embedding.Embedder;

import java.util.List;

@Service
public class QueryService {
    private static final int QUERY_RESPONSE_LIMIT = 5;
    private final Embedder embedder;
    private final QdrantDatabase qdrantDatabase;
    private final InstructionBuilder instructionBuilder;
    private final Conversation conversation;

    public QueryService(Embedder embedder, QdrantDatabase qdrantDatabase, InstructionBuilder instructionBuilder, Conversation conversation) {
        this.embedder = embedder;
        this.qdrantDatabase = qdrantDatabase;
        this.instructionBuilder = instructionBuilder;
        this.conversation = conversation;
    }

    public String query(List<String> allowedDocumentIds, String query, String collectionName) throws Exception {
        String refineQueryInstruction = instructionBuilder.buildQueryRefineInstruction();
        String refinedQuery = conversation.sendPrompt(refineQueryInstruction, query);
        IO.println("Refined Query: " + refinedQuery);
        List<float[]> embedding = embedder.getEmbeddings(List.of(refinedQuery));
        List<Chunk> matchingChunks = qdrantDatabase.searchInAll(collectionName, embedding.getFirst(), "documentId", allowedDocumentIds, QUERY_RESPONSE_LIMIT);
        String instruction = instructionBuilder.buildQueryInstruction(matchingChunks.stream().map(x -> x.payload().text()).toList());
        return conversation.sendPrompt(instruction, refinedQuery);
    }
}