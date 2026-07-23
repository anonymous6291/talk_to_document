package talktodocuments.talk_to_documents.home;

import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class LocalDocumentStorage {
    private static final String DOCUMENT_STORAGE_PATH = "./talk_to_document";

    public Path getLocalDocumentStoragePath(String userId, String documentName) {
        return Path.of(DOCUMENT_STORAGE_PATH, userId, documentName);
    }
}
