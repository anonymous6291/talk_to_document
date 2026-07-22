package talktodocuments.talk_to_documents;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import talktodocuments.talk_to_documents.database.data.user.SessionService;

import java.util.List;

@Controller
public class DocumentUpload {
    private final SessionService sessionService;
    private final DocumentHandler documentHandler;
    private final ObjectMapper jsonParser;

    public DocumentUpload(SessionService sessionService, DocumentHandler documentHandler) {
        this.sessionService = sessionService;
        this.documentHandler = documentHandler;
        jsonParser = new ObjectMapper();
        jsonParser.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @PostMapping("/upload/{section}")
    public ResponseEntity<?> uploadDocuments(@CookieValue(name = "email", required = false) String emailId, @CookieValue(name = "sessionId", required = false) String sessionId, @RequestParam(name = "files", required = false) MultipartFile[] files, @PathVariable(name = "section") String section) throws Exception {
        if (emailId == null || sessionId == null || !sessionService.isValidSession(emailId, sessionId) || files == null || files.length == 0 || section == null || section.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        List<JSONDocumentData> addedDocuments = documentHandler.processDocuments(emailId, section, files);
        return ResponseEntity.ok(jsonParser.writeValueAsString(new AddedDocumentsList(addedDocuments)));
    }

    record AddedDocumentsList(List<JSONDocumentData> addedDocuments) {
    }
}
