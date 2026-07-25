package talktodocuments.talk_to_documents.home;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructionBuilder {
    private static final String QUERY_REFINE_INSTRUCTION = """
            Assume you are going to talk to a person who like clear instructions, no extra bluff. I will give you my original query,
            your task is to refine the query into simpler query so that it doesn't lose it's actual meaning, context and keywords.
            DON'T SAY ANYTHING EXTRA, the output should ONLY contain the refined query.
            Remember that the resulting refined query SHOULD NOT LOSE the actual meaning, context and keywords of the original query.
            """;
    private static final String QUERY_INSTRUCTION = """
            Assume you are a smart record register that answers questions using only the data provided. I will ask you a question,
            you will answer my questions USING ONLY the following list of data set, DON'T use any of your previous knowledge to answer my questions.
            The data sets are:
            
            """;

    public String buildQueryRefineInstruction() {
        return QUERY_REFINE_INSTRUCTION;
    }

    public String buildQueryInstruction(List<String> chunks) {
        StringBuilder instructionString = new StringBuilder(QUERY_INSTRUCTION);
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
