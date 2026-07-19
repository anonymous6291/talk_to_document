package talktodocuments.talk_to_documents.database.data.chunk;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {
    public void deleteAllByDocumentId(String documentId);
}
