package talktodocuments.talk_to_documents;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import talktodocuments.talk_to_documents.models.conversational.Conversation;
import talktodocuments.talk_to_documents.models.embedding.EmbeddingData;
import talktodocuments.talk_to_documents.models.embedding.GemmaModel;

import java.util.List;

@RestController
public class CallMe {
    private final Conversation conversation;
    private final GemmaModel gemmaModel;

    public CallMe(Conversation conversation, GemmaModel gemmaModel) {
        this.conversation = conversation;
        this.gemmaModel = gemmaModel;
    }

    @GetMapping(path = "/")
    public String call() throws Exception {
        List<EmbeddingData> embeddingData = gemmaModel.getEmbedding(List.of("Hello World!", "How are you?"));
        String result = "";
        for (EmbeddingData e : embeddingData) {
            for (float f : e.embedding()) {
                result += f + ",";
            }
            result += "\n";
        }
        return result;
    }
}
