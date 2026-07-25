package talktodocuments.talk_to_documents;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(NoResourceFoundException.class)
    public String handleException(NoResourceFoundException e) {
        return "redirect:/error/error.html?code=404";
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        return "redirect:/error/error.html?code=500";
    }
}
