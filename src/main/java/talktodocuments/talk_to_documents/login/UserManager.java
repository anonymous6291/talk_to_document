package talktodocuments.talk_to_documents.login;

import org.springframework.stereotype.Service;
import talktodocuments.talk_to_documents.database.data.user.SessionService;
import talktodocuments.talk_to_documents.database.data.user.UserService;
import talktodocuments.talk_to_documents.database.embedding.QdrantDatabase;

@Service
public class UserManager {
    private final UserService userService;
    private final SessionService sessionService;
    private final QdrantDatabase qdrantDatabase;

    public UserManager(UserService userService, SessionService sessionService, QdrantDatabase qdrantDatabase) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.qdrantDatabase = qdrantDatabase;
    }

    public void addUser(String emailId, String password) throws Exception {
        userService.addUser(emailId, password);
        qdrantDatabase.addCollection(emailId);
    }

    public boolean isValidSession(String emailId, String sessionId) {
        if (emailId == null || sessionId == null) {
            return false;
        }
        return sessionService.isValidSession(emailId, sessionId);
    }
}
