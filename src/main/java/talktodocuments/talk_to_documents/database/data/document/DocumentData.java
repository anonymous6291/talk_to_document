package talktodocuments.talk_to_documents.database.data.document;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_data")
@Getter
@Setter
public class DocumentData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false, unique = true)
    private String documentId;
    @Column(nullable = false)
    private String documentName;
    @Column(nullable = false)
    private String section;
    @Column(nullable = false)
    private String creationDateTime;

    public DocumentData() {
    }

    public DocumentData(String userId, String documentId, String documentName, String section) {
        this.userId = userId;
        this.documentId = documentId;
        this.documentName = documentName;
        this.section = section;
        this.creationDateTime = LocalDateTime.now().toString();
    }
}
