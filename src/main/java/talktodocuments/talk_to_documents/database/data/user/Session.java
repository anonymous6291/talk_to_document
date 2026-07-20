package talktodocuments.talk_to_documents.database.data.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false, unique = true)
    private String sessionId;
    @Column(nullable = false)
    private Instant expiry;

    public Session() {
    }

    public Session(String email, Instant expiry) {
        this.email = email;
        this.sessionId = UUID.randomUUID().toString();
        this.expiry = expiry;
    }
}
