package talktodocuments.talk_to_documents.database.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByEmailAndSessionId(String email, String sessionId);

    void deleteByEmailAndSessionId(String email, String sessionId);

    void deleteAllByEmail(String email);
}