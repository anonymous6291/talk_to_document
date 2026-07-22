package talktodocuments.talk_to_documents;

import org.springframework.stereotype.Service;
import talktodocuments.talk_to_documents.database.data.user.UserService;
import talktodocuments.talk_to_documents.database.embedding.QdrantDatabase;

@Service
public class UserManager {
    private final UserService userService;
    private final QdrantDatabase qdrantDatabase;

    public UserManager(UserService userService, QdrantDatabase qdrantDatabase) {
        this.userService = userService;
        this.qdrantDatabase = qdrantDatabase;
    }

    public void addUser(String emailId, String password) throws Exception {
        userService.addUser(emailId, password);
        qdrantDatabase.addCollection(emailId);
    }
}
