package talktodocuments.talk_to_documents.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import talktodocuments.talk_to_documents.database.data.user.UserManager;

@Controller
public class HomePage {
    private final UserManager userManager;

    public HomePage(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping("/home")
    public String homePage(@CookieValue(name = "email", required = false) String emailId, @CookieValue(name = "sessionId", required = false) String sessionId) {
        if (!userManager.isValidSession(emailId, sessionId)) {
            return "redirect:/login";
        }
        return "home.html";
    }
}