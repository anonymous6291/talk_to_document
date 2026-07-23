package talktodocuments.talk_to_documents.database.data.document;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentDataRepository extends JpaRepository<DocumentData, Long> {
    List<DocumentData> findAllByUserId(String userId);

    long deleteByUserIdAndDocumentId(String userId, String documentId);

    boolean existsByUserIdAndDocumentId(String userId, String documentId);
}
