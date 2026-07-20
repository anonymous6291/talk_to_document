package talktodocuments.talk_to_documents.database.data.chunk;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
    @Column(nullable = false)
    private LocalDateTime registerDateAndTime;

    public RemovableChunk() {
    }

    public RemovableChunk(String chunkId, LocalDateTime registerDateAndTime) {
        this.chunkId = chunkId;
        this.registerDateAndTime = registerDateAndTime;
    }
}
