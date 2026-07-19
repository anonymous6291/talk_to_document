package talktodocuments.talk_to_documents.database.data.chunk;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RemovableChunkService {
    private final RemovableChunkRepository removableChunkRepository;

    public RemovableChunkService(RemovableChunkRepository removableChunkRepository) {
        this.removableChunkRepository = removableChunkRepository;
    }

    public List<String> getAllChunkIds() {
        List<RemovableChunk> removableChunks = removableChunkRepository.findAll();
        List<String> chunkIds = new LinkedList<>();
        removableChunks.forEach(x -> chunkIds.add(x.getChunkId()));
        return chunkIds;
    }

    public void deleteChunkId(String chunkId) {
        removableChunkRepository.deleteByChunkId(chunkId);
    }

    public void addChunkId(String chunkId) {
        removableChunkRepository.save(new RemovableChunk(chunkId));
    }
}
