package talktodocuments.talk_to_documents.database.data.chunk;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "removable_chunk")
@Getter
@Setter
public class RemovableChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true, nullable = false)
    private String chunkId;

    RemovableChunk(String chunkId) {
        this.chunkId = chunkId;
    }
}
