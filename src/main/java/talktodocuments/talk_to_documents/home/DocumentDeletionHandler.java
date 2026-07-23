package talktodocuments.talk_to_documents.home;

import org.springframework.stereotype.Service;
import talktodocuments.talk_to_documents.database.data.chunk.DocumentChunk;
import talktodocuments.talk_to_documents.database.data.chunk.DocumentChunkService;
import talktodocuments.talk_to_documents.database.data.document.DocumentDataService;
import talktodocuments.talk_to_documents.database.embedding.QdrantDatabase;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class DocumentDeletionHandler {
    private final DocumentDataService documentDataService;
    private final DocumentChunkService documentChunkService;
    private final QdrantDatabase qdrantDatabase;
    private final LocalDocumentStorage localDocumentStorage;

    public DocumentDeletionHandler(DocumentDataService documentDataService, DocumentChunkService documentChunkService, QdrantDatabase qdrantDatabase, LocalDocumentStorage localDocumentStorage) {
        this.documentDataService = documentDataService;
        this.documentChunkService = documentChunkService;
        this.qdrantDatabase = qdrantDatabase;
        this.localDocumentStorage = localDocumentStorage;
    }

    public void deleteDocuments(String userId, List<String> documentDataList) {
        for (String documentData : documentDataList) {
            try {
                deleteDocument(userId, documentData);
            } catch (Exception e) {
                IO.println(e);
            }
        }
    }

    public void deleteDocument(String userId, String documentId) throws Exception {
        if (!documentDataService.deleteDocumentData(userId, documentId)) {
            return;
        }
        List<DocumentChunk> documentChunks = documentChunkService.getAllChunksForDocumentId(documentId);
        documentChunkService.deleteAllChunksForDocumentId(documentId);
        qdrantDatabase.deleteChunks(userId, documentChunks.stream().map(DocumentChunk::getChunkId).toList());
        Path localFilePath = localDocumentStorage.getLocalDocumentStoragePath(userId, documentId);
        Files.delete(localFilePath);
    }
}
