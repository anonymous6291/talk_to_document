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
    public void addAllChunks(String documentId, List<String> chunkIds) {
        for (String chunkId : chunkIds) {
            documentChunkRepository.save(new DocumentChunk(documentId, chunkId));
        }
    }

    @Transactional
    public void deleteAllChunks(String documentId) {
        documentChunkRepository.deleteAllByDocumentId(documentId);
    }
}
