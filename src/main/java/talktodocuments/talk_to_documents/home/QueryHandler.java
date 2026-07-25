package talktodocuments.talk_to_documents.home;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import talktodocuments.talk_to_documents.database.data.document.DocumentDataService;
import talktodocuments.talk_to_documents.database.data.user.UserManager;

import java.util.List;

@RestController
public class QueryHandler {
    private final UserManager userManager;
    private final QueryService queryService;
    private final DocumentDataService documentDataService;
    private final ObjectMapper jsonParser;

    public QueryHandler(UserManager userManager, QueryService queryService, DocumentDataService documentDataService) {
        this.userManager = userManager;
        this.queryService = queryService;
        this.documentDataService = documentDataService;
        jsonParser = new ObjectMapper();
        jsonParser.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @PostMapping("/query")
    public String query(@CookieValue(name = "email", required = false) String emailId, @CookieValue(name = "sessionId", required = false) String sessionId, @RequestBody QueryData queryData) throws Exception {
        if (!userManager.isValidSession(emailId, sessionId)) {
            return jsonParser.writeValueAsString(new QueryResult(false, "Invalid request."));
        }
        for (String documentId : queryData.allowedDocuments()) {
            if (!documentDataService.documentIdExistsForUserId(emailId, documentId)) {
                return jsonParser.writeValueAsString(new QueryResult(false, "Document doesn't exists."));
            }
        }
        String response = queryService.query(queryData.allowedDocuments(), queryData.query, emailId);
        return jsonParser.writeValueAsString(new QueryResult(true, response));
    }

    public record QueryData(String query, List<String> allowedDocuments) {
    }

    record QueryResult(boolean success, String response) {
    }
}
