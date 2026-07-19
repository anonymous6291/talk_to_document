package talktodocuments.talk_to_documents.database.data.chunk;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RemovableChunkRepository extends JpaRepository<RemovableChunk, Long> {
    void deleteByChunkId(String chunkId);
}
