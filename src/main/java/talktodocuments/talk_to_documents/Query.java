package talktodocuments.talk_to_documents;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Query {
    @PostMapping("/query")
    public String query() {
        return "{\"response\":\"Hello World!!!!\"}";
    }
}
