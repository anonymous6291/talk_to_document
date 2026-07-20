package talktodocuments.talk_to_documents.database.data.chunk;

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

    public void addRemovableChunk(String chunkId) {
        removableChunkRepository.save(new RemovableChunk(chunkId, LocalDateTime.now()));
    }

    public void deleteRemovableChunk(String chunkId) {
        removableChunkRepository.deleteByChunkId(chunkId);
    }
}
