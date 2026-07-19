package talktodocuments.talk_to_documents.models.conversational;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import tools.jackson.databind.util.JSONPObject;
import tools.jackson.databind.util.JSONWrappedObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
public class QwenModel {
    private final static String LLM_URL = "http://localhost:9000/v1/chat/completions";
    private final static String MODEL_NAME = "qwen2.5-3b";
    private final static float TEMPERATURE = 0.3F;
    private final static int MAX_TOKENS = 2056;
    private final static boolean STREAM = false;
    private final HttpClient llmConnection;
    private final ObjectMapper jsonParser;

    public QwenModel() {
        llmConnection = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        jsonParser = new ObjectMapper();
        jsonParser.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String sendPrompt(List<ConversationMessage> queries) throws Exception {
        ModelJSONQuery modelJSONQuery = new ModelJSONQuery(MODEL_NAME, queries, TEMPERATURE, MAX_TOKENS, STREAM);
        String modelJSONQueryString = jsonParser.writeValueAsString(modelJSONQuery);
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(LLM_URL)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(modelJSONQueryString)).build();
        HttpResponse<String> httpResponse = llmConnection.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String responseString = httpResponse.body();
        return jsonParser.readTree(responseString).findPath("message").findPath("content").toString();
    }

    record ModelJSONQuery(String model, List<ConversationMessage> messages, float temperature,
                          int max_tokens, boolean stream) {
    }
}

/*

{"choices":[{"finish_reason":"stop","index":0,"message":{"role":"assistant","content":"Hello! How can I assist you today?"}}],"created":1784442078,"model":"Qwen2.5-Coder-3B-Instruct-Q8_0.gguf","system_fingerprint":"b8855-81df3f7cf","object":"chat.completion","usage":{"completion_tokens":10,"prompt_tokens":9,"total_tokens":19,"prompt_tokens_details":{"cached_tokens":0}},"id":"chatcmpl-g9YaE7efqXGhdCG6EX8p7pBoTGtlLTXx","timings":{"cache_n":0,"prompt_n":9,"prompt_ms":6375.805,"prompt_per_token_ms":708.4227777777778,"prompt_per_second":1.4115864584942606,"predicted_n":10,"predicted_ms":44895.895,"predicted_per_token_ms":4489.5895,"predicted_per_second":0.22273751308443682}}

 */

/*
{
 "model":"qwen2.5-3b",
 "messages":[
    {
       "role":"system",
       "content":"You are kali linux automation agent. When needed to run the command, use format: ACTION:[command]. Then wait for the        OBSERVATION."
    },
    {
        "role":"user",
        "content":"List all java files"
    }
 ],
 "temperature":0.0,
 "max_tokens":512,
 "stream":true,
 "stop":["OBSERVATION:"]
}
 */