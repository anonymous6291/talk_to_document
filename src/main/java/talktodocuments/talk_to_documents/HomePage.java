package talktodocuments.talk_to_documents;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import talktodocuments.talk_to_documents.database.data.user.SessionService;

@Controller
public class HomePage {
    private final SessionService sessionService;

    public HomePage(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/home")
    public String homePage(@CookieValue(name = "email", required = false) String emailId, @CookieValue(name = "sessionId", required = false) String sessionId) {
        if (emailId == null || sessionId == null || !sessionService.isValidSession(emailId, sessionId)) {
            return "redirect:/login";
        }
        return "home.html";
    }
}