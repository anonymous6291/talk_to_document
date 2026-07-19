package talktodocuments.talk_to_documents.models.conversational;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Conversation {
    private final QwenModel qwenModel;
    private final String instruction = "";

    public Conversation(QwenModel qwenModel) {
        this.qwenModel = qwenModel;
    }

    public String sendPrompt(List<String> chunks, String query) throws Exception {
        String instruction = buildInstruction(chunks, query);
        ConversationMessage conversationMessage = new ConversationMessage("system", instruction);
        return qwenModel.sendPrompt(List.of(conversationMessage));
    }

    private String buildInstruction(List<String> chunks, String query) {
        return "Hello";
    }
}

