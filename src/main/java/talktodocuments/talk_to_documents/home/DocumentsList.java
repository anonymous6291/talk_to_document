package talktodocuments.talk_to_documents.home;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import talktodocuments.talk_to_documents.database.data.document.DocumentData;
import talktodocuments.talk_to_documents.database.data.document.DocumentDataService;
import talktodocuments.talk_to_documents.database.data.user.SessionService;

import java.util.LinkedList;
import java.util.List;

@RestController
public class DocumentsList {
    private final SessionService sessionService;
    private final DocumentDataService documentDataService;
    private final ObjectMapper jsonHandler;

    public DocumentsList(SessionService sessionService, DocumentDataService documentDataService) {
        this.sessionService = sessionService;
        this.documentDataService = documentDataService;
        jsonHandler = new ObjectMapper();
        jsonHandler.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @GetMapping("/documents")
    public String getDocuments(@CookieValue(name = "email", required = false) String email, @CookieValue(name = "sessionId", required = false) String sessionId) throws Exception {
        if (email == null || sessionId == null) {
            return "{\"type\":\"invalid\"}";
        }
        if (!sessionService.isValidSession(email, sessionId)) {
            return "{\"type\":\"error\"}";
        }
        List<DocumentData> documentData = documentDataService.getAllDocumentDataForUserId(email);
        List<JSONDocumentData> jsonDocumentData = new LinkedList<>();
        documentData.forEach(x -> jsonDocumentData.add(new JSONDocumentData(x.getDocumentName(), x.getDocumentId(), x.getSection(), x.getCreationDateTime().toString())));
        JSONDocumentDataList jsonDocumentDataList = new JSONDocumentDataList(jsonDocumentData);
        return jsonHandler.writeValueAsString(jsonDocumentDataList);
    }
}

record JSONDocumentDataList(List<JSONDocumentData> documents) {
}