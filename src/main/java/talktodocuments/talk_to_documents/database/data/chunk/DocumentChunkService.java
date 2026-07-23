package talktodocuments.talk_to_documents.database.data.chunk;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentChunkService {
    private final DocumentChunkRepository documentChunkRepository;

    public DocumentChunkService(DocumentChunkRepository documentChunkRepository) {
        this.documentChunkRepository = documentChunkRepository;
    }

    @Transactional
    public void addAllChunksForDocumentId(String userId, String documentId, List<String> chunkIds) {
        for (String chunkId : chunkIds) {
            documentChunkRepository.save(new DocumentChunk(userId, documentId, chunkId));
        }
    }

    public List<DocumentChunk> getAllChunksForDocumentId(String documentId) {
        return documentChunkRepository.findAllByDocumentId(documentId);
    }

    @Transactional
    public void deleteAllChunksForDocumentId(String documentId) {
        documentChunkRepository.deleteAllByDocumentId(documentId);
    }
}
