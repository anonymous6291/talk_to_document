package talktodocuments.talk_to_documents.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import talktodocuments.talk_to_documents.database.data.user.Session;
import talktodocuments.talk_to_documents.database.data.user.UserManager;

import java.time.Duration;
import java.time.Instant;

@Controller
public class Login {
    private final UserManager userManager;

    public Login(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping({"/", "/login"})
    public String loginPage(@CookieValue(name = "email", required = false) String emailId, @CookieValue(name = "sessionId", required = false) String sessionId) throws Exception {
        if (userManager.isValidSession(emailId, sessionId)) {
            return "forward:/home";
        }
        userManager.addUser("1@gmail.com", "12345");
        return "login.html";
    }

    @PostMapping({"/", "/login"})
    public void doLogin(@RequestParam(name = "email", required = false) String emailId, @RequestParam(name = "password", required = false) String password, HttpServletResponse httpServletResponse) throws Exception {
        Session session = userManager.validateUserAndCreateNewSession(emailId, password);
        if (session == null) {
            httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(), "Invalid email or password.");
            return;
        }
        Instant expiry = session.getExpiry();
        Instant now = Instant.now();
        int cookieAge = (int) Math.min(Integer.MAX_VALUE, Math.abs(Duration.between(now, expiry).getSeconds()));
        Cookie emailCookie = new Cookie("email", emailId);
        emailCookie.setMaxAge(cookieAge);
        Cookie sessionCookie = new Cookie("sessionId", session.getSessionId());
        sessionCookie.setMaxAge(cookieAge);
        httpServletResponse.addCookie(emailCookie);
        httpServletResponse.addCookie(sessionCookie);
        httpServletResponse.sendRedirect("/home");
    }
}
