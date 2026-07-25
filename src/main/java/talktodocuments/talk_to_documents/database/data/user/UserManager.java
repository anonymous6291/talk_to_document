package talktodocuments.talk_to_documents.database.data.user;

import org.springframework.stereotype.Service;
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

    public boolean isValidUser(String emailId, String password) {
        return userService.validateUserPassword(emailId, password);
    }

    public boolean isValidSession(String emailId, String sessionId) {
        if (emailId == null || sessionId == null) {
            return false;
        }
        return sessionService.isValidSession(emailId, sessionId);
    }

    public Session createNewSession(String emailId) {
        return sessionService.createNewSession(emailId);
    }

    public Session validateUserAndCreateNewSession(String emailId, String password) {
        if (!isValidUser(emailId, password)) {
            return null;
        }
        return createNewSession(emailId);
    }
}
