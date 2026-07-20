package talktodocuments.talk_to_documents.database.data.user;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class SessionService {
    private static final Duration EXPIRY = Duration.ofDays(20);
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session createNewSession(String email) {
        Instant expiry = Instant.now().plus(EXPIRY);
        Session session = new Session(email, expiry);
        sessionRepository.save(session);
        return session;
    }

    public boolean isValidSession(String email, String sessionId) {
        Instant now = Instant.now();
        Optional<Session> optionalSession = sessionRepository.findByEmailAndSessionId(email, sessionId);
        if (optionalSession.isEmpty()) {
            return false;
        }
        Session session = optionalSession.get();
        Instant expiry = session.getExpiry();
        if (!now.isAfter(expiry)) {
            return true;
        }
        sessionRepository.delete(session);
        return false;
    }

    public void deleteSession(String email, String sessionId) {
        sessionRepository.deleteByEmailAndSessionId(email, sessionId);
    }

    public void deleteAllSessions(String email) {
        sessionRepository.deleteAllByEmail(email);
    }
}
