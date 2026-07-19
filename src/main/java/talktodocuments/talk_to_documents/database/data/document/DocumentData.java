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
    @Column(nullable = false, unique = true)
    private String documentId;
    @Column(nullable = false)
    private String documentName;
    @Column(nullable = false)
    private String path;
    @Column(nullable = false)
    private LocalDateTime creationDateTime;

    public DocumentData(String documentId, String documentName, String path) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.path = path;
        this.creationDateTime = LocalDateTime.now();
    }
}
