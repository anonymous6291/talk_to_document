package talktodocuments.talk_to_documents.home;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import talktodocuments.talk_to_documents.chunkers.Chunkers;
import talktodocuments.talk_to_documents.database.data.chunk.DocumentChunkService;
import talktodocuments.talk_to_documents.database.data.chunk.RemovableChunkService;
import talktodocuments.talk_to_documents.database.data.document.DocumentData;
import talktodocuments.talk_to_documents.database.data.document.DocumentDataService;
import talktodocuments.talk_to_documents.database.embedding.Chunk;
import talktodocuments.talk_to_documents.database.embedding.ChunkPayload;
import talktodocuments.talk_to_documents.database.embedding.QdrantDatabase;
import talktodocuments.talk_to_documents.models.embedding.Embedder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DocumentHandler {
    private final AtomicInteger documentNumber = new AtomicInteger();
    private final Chunkers chunkers;
    private final DocumentChunkService documentChunkService;
    private final RemovableChunkService removableChunkService;
    private final DocumentDataService documentDataService;
    private final QdrantDatabase qdrantDatabase;
    private final Embedder embedder;
    private final LocalDocumentStorage localDocumentStorage;

    public DocumentHandler(Chunkers chunkers, DocumentChunkService documentChunkService, RemovableChunkService removableChunkService, DocumentDataService documentDataService, QdrantDatabase qdrantDatabase, Embedder embedder, LocalDocumentStorage localDocumentStorage) {
        this.chunkers = chunkers;
        this.documentChunkService = documentChunkService;
        this.removableChunkService = removableChunkService;
        this.documentDataService = documentDataService;
        this.qdrantDatabase = qdrantDatabase;
        this.embedder = embedder;
        this.localDocumentStorage = localDocumentStorage;
    }

    public List<JSONDocumentData> processDocuments(String userId, String section, MultipartFile[] files) {
        List<JSONDocumentData> jsonDocumentDataList = new LinkedList<>();
        for (MultipartFile file : files) {
            try {
                JSONDocumentData jsonDocumentData = processSingleDocument(userId, section, file);
                if (jsonDocumentData != null) {
                    jsonDocumentDataList.add(jsonDocumentData);
                }
            } catch (Exception e) {
                IO.println(e);
            }
        }
        return jsonDocumentDataList;
    }

    private JSONDocumentData processSingleDocument(String userId, String section, MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return null;
        }
        String originalFileName = Path.of(fileName).getFileName().toString();
        String documentId = UUID.randomUUID().toString().concat(Integer.toString(documentNumber.incrementAndGet()));
        Path targetFilePath = localDocumentStorage.getLocalDocumentStoragePath(userId, documentId);
        Files.createDirectories(targetFilePath.getParent());
        file.transferTo(targetFilePath);
        List<String> textChunks = chunkers.getChunks(targetFilePath.toFile(), originalFileName);
        int numberOfChunks = textChunks.size();
        List<float[]> embeddings = embedder.getEmbeddings(textChunks);
        List<String> chunkIds = generateChunkIds(documentId, numberOfChunks);
        List<Chunk> chunks = new ArrayList<>();
        for (int i = 0; i < numberOfChunks; i++) {
            String chunkId = chunkIds.get(i);
            chunks.add(new Chunk(chunkId, embeddings.get(i), new ChunkPayload(textChunks.get(i), documentId)));
            removableChunkService.addRemovableChunk(userId, chunkId);
        }
        qdrantDatabase.addChunks(userId, chunks);
        documentChunkService.addAllChunksForDocumentId(userId, documentId, chunkIds);
        DocumentData documentData = documentDataService.addDocumentDataForUserId(userId, originalFileName, documentId, section);
        chunkIds.forEach(removableChunkService::deleteRemovableChunk);
        return new JSONDocumentData(originalFileName, documentId, section, documentData.getCreationDateTime());
    }

    private List<String> generateChunkIds(String documentId, int numberOfChunks) {
        List<String> chunkIds = new ArrayList<>(numberOfChunks);
        while (numberOfChunks > 0) {
            chunkIds.add(UUID.randomUUID().toString());
            numberOfChunks--;
        }
        return chunkIds;
    }
}
