package talktodocuments.talk_to_documents;

import org.springframework.stereotype.Service;
import talktodocuments.talk_to_documents.database.data.chunk.RemovableChunk;
import talktodocuments.talk_to_documents.database.data.chunk.RemovableChunkService;
import talktodocuments.talk_to_documents.database.data.document.DocumentData;
import talktodocuments.talk_to_documents.database.data.document.DocumentDataService;
import talktodocuments.talk_to_documents.database.data.user.User;
import talktodocuments.talk_to_documents.database.data.user.UserService;
import talktodocuments.talk_to_documents.database.embedding.QdrantDatabase;
import talktodocuments.talk_to_documents.home.LocalDocumentStorage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CleanupService {
    private static final boolean RUN_ON_STARTUP = true;
    private static final Duration CLEAN_UP_DELAY = Duration.ofMinutes(5);
    private static final Duration DELETE_REMOVABLE_CHUNKS_AFTER = Duration.ofMinutes(10);
    private final UserService userService;
    private final RemovableChunkService removableChunkService;
    private final DocumentDataService documentDataService;
    private final QdrantDatabase qdrantDatabase;
    private final LocalDocumentStorage localDocumentStorage;

    public CleanupService(UserService userService, RemovableChunkService removableChunkService, DocumentDataService documentDataService, QdrantDatabase qdrantDatabase, LocalDocumentStorage localDocumentStorage) throws Exception {
        this.userService = userService;
        this.removableChunkService = removableChunkService;
        this.documentDataService = documentDataService;
        this.qdrantDatabase = qdrantDatabase;
        this.localDocumentStorage = localDocumentStorage;
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        long delay = CLEAN_UP_DELAY.getSeconds();
        scheduledExecutorService.scheduleWithFixedDelay(this::cleanUP, RUN_ON_STARTUP ? 0 : delay, delay, TimeUnit.SECONDS);
    }

    private void cleanUP() {
        try {
            printAllQdrantCollections();
            Set<String> userIds = userService.getAllUsers().stream().map(User::getEmail).collect(Collectors.toSet());
            LocalDateTime nonExpiryTime = LocalDateTime.now().minus(DELETE_REMOVABLE_CHUNKS_AFTER);
            List<RemovableChunk> removableChunks = removableChunkService.getAllRemovableChunks();
            List<RemovableChunk> removableChunksToBeDeleted = removableChunks.stream().filter(chunk -> !userIds.contains(chunk.getUserId()) || chunk.getRegisterDateAndTime().isBefore(nonExpiryTime)).toList();
            for (RemovableChunk removableChunk : removableChunksToBeDeleted) {
                qdrantDatabase.deleteChunks(removableChunk.getUserId(), List.of(removableChunk.getChunkId()));
            }
            List<DocumentData> documentDataList = documentDataService.getEntireDocumentData();
            Set<String> documentIds = documentDataList.stream().map(DocumentData::getDocumentId).collect(Collectors.toSet());
            Path documentStoragePath = localDocumentStorage.getLocalDocumentStoragePath("", "");
            cleanUpUnnecessaryFiles(documentStoragePath, documentIds, true);
            for (String collection : qdrantDatabase.getAllCollections()) {
                if (!userIds.contains(collection)) {
                    IO.println("Delete: " + collection);
                    qdrantDatabase.deleteCollection(collection);
                }
            }
            IO.println("Final Collection:");
            printAllQdrantCollections();
        } catch (Exception e) {
            IO.println(e);
        }
    }

    private void printAllQdrantCollections() {
        try {
            List<String> collections = qdrantDatabase.getAllCollections();
            for (String collectionName : collections) {
                String info = qdrantDatabase.getCollectionInfoAsJson(collectionName);
                IO.println(collectionName + ":");
                IO.println(info);
            }
        } catch (Exception e) {
            IO.println(e);
        }
    }

    private void cleanUpUnnecessaryFiles(Path current, Set<String> skipFiles, boolean skipDelete) {
        try {
            if (Files.isDirectory(current)) {
                Files.list(current).forEach(subPath -> cleanUpUnnecessaryFiles(subPath, skipFiles, false));
            } else if (skipFiles.contains(current.getFileName().toString())) {
                return;
            }
            if (!skipDelete) {
                Files.delete(current);
            }
        } catch (Exception _) {
        }
    }
}
