package talktodocuments.talk_to_documents;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import talktodocuments.talk_to_documents.database.data.user.Session;
import talktodocuments.talk_to_documents.database.data.user.SessionService;
import talktodocuments.talk_to_documents.database.data.user.UserService;

import java.time.Duration;
import java.time.Instant;

@Controller
public class Login {
    private final UserService userService;
    private final SessionService sessionService;

    public Login(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping({"/", "/login"})
    public String loginPage(@CookieValue(name = "email", required = false) String emailId, @CookieValue(name = "sessionId", required = false) String sessionId) {
        if (emailId != null && sessionId != null && sessionService.isValidSession(emailId, sessionId)) {
            return "forward:/home";
        }
        return "html/login.html";
        //userService.addUser("1@gmail.com", "12345");
    }

    @PostMapping({"/", "/login"})
    public void doLogin(@RequestParam(name = "email", required = false) String email, @RequestParam(name = "password", required = false) String password, HttpServletResponse httpServletResponse) throws Exception {
        if (email == null || password == null || !userService.validateUserPassword(email, password)) {
            httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(), "Invalid email or password.");
            return;
        }
        Session session = sessionService.createNewSession(email);
        Instant expiry = session.getExpiry();
        Instant now = Instant.now();
        int cookieAge = (int) Math.min(Integer.MAX_VALUE, Math.abs(Duration.between(now, expiry).getSeconds()));
        Cookie emailCookie = new Cookie("email", email);
        emailCookie.setMaxAge(cookieAge);
        Cookie sessionCookie = new Cookie("sessionId", session.getSessionId());
        sessionCookie.setMaxAge(cookieAge);
        httpServletResponse.addCookie(emailCookie);
        httpServletResponse.addCookie(sessionCookie);
        httpServletResponse.setStatus(200);
        httpServletResponse.sendRedirect("/home");
    }
}
