package talktodocuments.talk_to_documents.home;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import talktodocuments.talk_to_documents.database.data.document.DocumentData;
import talktodocuments.talk_to_documents.database.data.document.DocumentDataService;
import talktodocuments.talk_to_documents.database.data.user.UserManager;

import java.util.LinkedList;
import java.util.List;

@RestController
public class DocumentsList {
    private final UserManager userManager;
    private final DocumentDataService documentDataService;
    private final ObjectMapper jsonHandler;

    public DocumentsList(UserManager userManager, DocumentDataService documentDataService) {
        this.userManager = userManager;
        this.documentDataService = documentDataService;
        jsonHandler = new ObjectMapper();
        jsonHandler.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @GetMapping("/documents")
    public String getDocuments(@CookieValue(name = "email", required = false) String email, @CookieValue(name = "sessionId", required = false) String sessionId) throws Exception {
        if (!userManager.isValidSession(email, sessionId)) {
            return "{\"type\":\"invalid\"}";
        }
        List<DocumentData> documentData = documentDataService.getAllDocumentDataForUserId(email);
        List<JSONDocumentData> jsonDocumentData = new LinkedList<>();
        documentData.forEach(x -> jsonDocumentData.add(new JSONDocumentData(x.getDocumentName(), x.getDocumentId(), x.getSection(), x.getCreationDateTime())));
        JSONDocumentDataList jsonDocumentDataList = new JSONDocumentDataList(jsonDocumentData);
        return jsonHandler.writeValueAsString(jsonDocumentDataList);
    }
}

record JSONDocumentDataList(List<JSONDocumentData> documents) {
}