package talktodocuments.talk_to_documents.database.data.chunk;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "document_chunk")
@Getter
@Setter
public class DocumentChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String documentId;
    @Column(nullable = false, unique = true)
    private String chunkId;

    public DocumentChunk() {
    }

    public DocumentChunk(String documentId, String chunkId) {
        this.documentId = documentId;
        this.chunkId = chunkId;
    }
}
