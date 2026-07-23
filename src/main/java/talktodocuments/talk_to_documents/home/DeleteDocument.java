package talktodocuments.talk_to_documents.home;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import talktodocuments.talk_to_documents.login.UserManager;

import java.util.List;

@RestController
public class DeleteDocument {
    private final UserManager userManager;
    private final ObjectMapper jsonHandler;
    private final DocumentDeletionHandler documentDeletionHandler;

    public DeleteDocument(UserManager userManager, DocumentDeletionHandler documentDeletionHandler) {
        this.userManager = userManager;
        this.documentDeletionHandler = documentDeletionHandler;
        jsonHandler = new ObjectMapper();
        jsonHandler.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @PostMapping("/deleteDocuments")
    public String deleteDocuments(@CookieValue(name = "email", required = false) String emailId, @CookieValue(name = "sessionId") String sessionId, @RequestBody DeleteDocumentList deleteDocumentList) throws Exception {
        if (!userManager.isValidSession(emailId, sessionId)) {
            return jsonHandler.writeValueAsString(new Response(false));
        }
        documentDeletionHandler.deleteDocuments(emailId, deleteDocumentList.documentIds());
        return jsonHandler.writeValueAsString(new Response(true));
    }

    public record DeleteDocumentList(List<String> documentIds) {
    }

    record Response(boolean success) {
    }
}