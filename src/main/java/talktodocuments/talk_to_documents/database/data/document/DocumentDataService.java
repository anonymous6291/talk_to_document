package talktodocuments.talk_to_documents.database.data.document;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentDataService {
    private final DocumentDataRepository documentDataRepository;

    public DocumentDataService(DocumentDataRepository documentDataRepository) {
        this.documentDataRepository = documentDataRepository;
    }

    public List<DocumentData> getAllDocumentDataForUserId(String userId) {
        return documentDataRepository.findAllByUserId(userId);
    }
}
