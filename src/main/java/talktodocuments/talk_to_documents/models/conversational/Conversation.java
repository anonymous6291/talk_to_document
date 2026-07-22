package talktodocuments.talk_to_documents.models.conversational;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Conversation {
    private static final String instruction = """
            You are my advisor and I want to ask you few questions.
            You will answer my questions using only the following data, don't use your
            previous knowledge to answer my questions.
            """;
    private final QwenModel qwenModel;

    public Conversation(QwenModel qwenModel) {
        this.qwenModel = qwenModel;
    }

    public String sendPrompt(List<String> chunks, String query) throws Exception {
        String instruction = buildInstruction(chunks);
        ConversationMessage instructionMessage = new ConversationMessage("system", instruction);
        ConversationMessage queryMessage = new ConversationMessage("user", query);
        return qwenModel.sendPrompt(List.of(instructionMessage, queryMessage));
    }

    private String buildInstruction(List<String> chunks) {
        StringBuilder instructionString = new StringBuilder(instruction);
        int i = 1;
        for (String chunk : chunks) {
            instructionString.append("\n");
            instructionString.append("Data ".concat(Integer.toString(i++)).concat(" :"));
            instructionString.append("\n");
            instructionString.append(chunk);
        }
        return instructionString.toString();
    }
}

