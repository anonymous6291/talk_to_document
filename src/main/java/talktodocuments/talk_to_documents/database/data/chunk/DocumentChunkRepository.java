package talktodocuments.talk_to_documents.database.data.chunk;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {
    List<DocumentChunk> findAllByDocumentId(String documentId);

    void deleteAllByDocumentId(String documentId);
}
