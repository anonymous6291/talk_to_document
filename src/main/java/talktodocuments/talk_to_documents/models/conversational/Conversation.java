package talktodocuments.talk_to_documents.models.conversational;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Conversation {
    private final QwenModel qwenModel;

    public Conversation(QwenModel qwenModel) {
        this.qwenModel = qwenModel;
    }

    public String sendPrompt(String instruction, String query) throws Exception {
        ConversationMessage instructionMessage = new ConversationMessage("system", instruction);
        ConversationMessage queryMessage = new ConversationMessage("user", query);
        return qwenModel.sendPrompt(List.of(instructionMessage, queryMessage));
    }
}

