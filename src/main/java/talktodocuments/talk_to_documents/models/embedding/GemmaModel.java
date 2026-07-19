package talktodocuments.talk_to_documents.models.embedding;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
public class GemmaModel {
    private static final String LLM_URL = "http://127.0.0.1:9001/v1/embeddings";
    private static final String MODEL_NAME = "gemma-embedding";
    private final HttpClient llmClient;
    private final ObjectMapper jsonParser;

    public GemmaModel() {
        llmClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        jsonParser = new ObjectMapper();
        jsonParser.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<EmbeddingData> getEmbedding(List<String> inputs) throws Exception {
        EmbeddingModelQuery embeddingModelQuery = new EmbeddingModelQuery(MODEL_NAME, inputs);
        String queryString = jsonParser.writeValueAsString(embeddingModelQuery);
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(LLM_URL)).POST(HttpRequest.BodyPublishers.ofString(queryString)).build();
        HttpResponse<String> httpResponse = llmClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String responseString = httpResponse.body();
        EmbeddingModelResponse embeddingModelResponse = jsonParser.readValue(responseString, EmbeddingModelResponse.class);
        return embeddingModelResponse.data;
    }

    record EmbeddingModelQuery(String model, List<String> input) {
    }

    record EmbeddingModelResponse(String object, List<EmbeddingData> data) {
    }
}
/*

{
  "model": "gemma-embedding",
  "input": [
    "First document",
    "Second document",
    "Third document"
  ]
}




{
  "object": "list",
  "data": [
    {
      "index": 0,
      "embedding": [ ... ]
    },
    {
      "index": 1,
      "embedding": [ ... ]
    },
    {
      "index": 2,
      "embedding": [ ... ]
    }
  ]
}
 */