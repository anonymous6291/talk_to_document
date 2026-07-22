package talktodocuments.talk_to_documents.database.data.document;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentDataService {
    private final DocumentDataRepository documentDataRepository;

    public DocumentDataService(DocumentDataRepository documentDataRepository) {
        this.documentDataRepository = documentDataRepository;
    }

    @Transactional
    public DocumentData addDocumentDataForUserId(String userId, String documentName, String documentId, String section) {
        DocumentData documentData = new DocumentData(userId, documentId, documentName, section);
        documentDataRepository.save(documentData);
        return documentData;
    }

    public List<DocumentData> getAllDocumentDataForUserId(String userId) {
        return documentDataRepository.findAllByUserId(userId);
    }

    public boolean documentIdExistsForUserId(String userId, String documentId) {
        return documentDataRepository.existsByUserIdAndDocumentId(userId, documentId);
    }
}
