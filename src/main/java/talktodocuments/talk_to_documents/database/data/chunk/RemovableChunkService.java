package talktodocuments.talk_to_documents.database.data.chunk;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RemovableChunkService {
    private final RemovableChunkRepository removableChunkRepository;

    public RemovableChunkService(RemovableChunkRepository removableChunkRepository) {
        this.removableChunkRepository = removableChunkRepository;
    }

    public List<RemovableChunk> getAllRemovableChunks() {
        return removableChunkRepository.findAll();
    }

    @Transactional
    public void addRemovableChunk(String userId, String chunkId) {
        removableChunkRepository.save(new RemovableChunk(userId, chunkId, LocalDateTime.now()));
    }

    @Transactional
    public void deleteRemovableChunk(String chunkId) {
        removableChunkRepository.deleteByChunkId(chunkId);
    }
}
