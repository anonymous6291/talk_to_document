package talktodocuments.talk_to_documents.database.embedding;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

@Service
public class QdrantDatabase {
    private static final int VECTOR_SIZE = 768;
    private static final int QUERY_RESPONSE_LIMIT = 5;
    private static final boolean QUERY_RESPONSE_WITH_VECTOR = false;
    private static final boolean QUERY_RESPONSE_WITH_PAYLOAD = true;
    private static final String SIMILARITY_MATCHING_METHOD = "Cosine";
    private static final String BASE_URL = "http://127.0.0.1:6334/collections/";
    private static final String ADD_COLLECTION_URL_EXTENSION = "";
    private static final String ADD_POINT_URL_EXTENSION = "/points";
    private static final String QUERY_POINT_URL_EXTENSION = "/points/query";
    private static final String DELETE_POINT_URL_EXTENSION = "/points/delete";
    private final HttpClient httpClient;
    private final ObjectMapper jsonParser;

    QdrantDatabase() {
        httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        jsonParser = new ObjectMapper();
        jsonParser.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public boolean addCollection(String collectionName) throws Exception {
        NewCollectionRequest newCollectionRequest = new NewCollectionRequest(new NewCollectionRequestVector(VECTOR_SIZE, SIMILARITY_MATCHING_METHOD));
        String requestJson = jsonParser.writeValueAsString(newCollectionRequest);
        String url = BASE_URL + collectionName + ADD_COLLECTION_URL_EXTENSION;
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url)).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(requestJson)).build();
        HttpResponse<Void> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
        return httpResponse.statusCode() == HttpStatus.OK.value();
    }

    public boolean addChunks(String collectionName, List<Chunk> chunks) throws Exception {
        ChunkData chunkData = new ChunkData(chunks);
        String chunkDataString = jsonParser.writeValueAsString(chunkData);
        String url = BASE_URL + collectionName + ADD_POINT_URL_EXTENSION;
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url)).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(chunkDataString)).build();
        HttpResponse<Void> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
        return httpResponse.statusCode() == 200;
    }

    public List<Chunk> searchInAllDocumentIds(String collectionName, float[] vector, String payloadFieldName, String... matchValues) throws Exception {
        List<KeyMatchData> keyMatchData = new LinkedList<>();
        for (String documentId : matchValues) {
            keyMatchData.add(new KeyMatchData(payloadFieldName, new MatchPayloadData(documentId)));
        }
        ShouldQueryData shouldQueryData = new ShouldQueryData(vector, QUERY_RESPONSE_LIMIT, QUERY_RESPONSE_WITH_PAYLOAD, QUERY_RESPONSE_WITH_VECTOR, keyMatchData);
        String shouldQueryDataString = jsonParser.writeValueAsString(shouldQueryData);
        String url = BASE_URL + collectionName + QUERY_POINT_URL_EXTENSION;
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(url)).build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        QueryResponse queryResponse = jsonParser.readValue(httpResponse.body(), QueryResponse.class);
        return queryResponse.result();
    }

    public boolean deleteChunks(String collectionName, List<String> chunkIds) throws Exception {
        DeleteChunkData deleteChunkData = new DeleteChunkData(chunkIds);
        String deleteChunkDataString = jsonParser.writeValueAsString(deleteChunkData);
        String url = BASE_URL + collectionName + DELETE_POINT_URL_EXTENSION;
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(deleteChunkDataString)).build();
        HttpResponse<Void> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
        return httpResponse.statusCode() == 200;
    }

    record NewCollectionRequestVector(int size, String distance) {
    }

    record NewCollectionRequest(NewCollectionRequestVector vectors) {
    }

    record ChunkData(List<Chunk> points) {
    }

    record DeleteChunkData(List<String> points) {
    }

    record MatchPayloadData(String value) {
    }

    record KeyMatchData(String key, MatchPayloadData match) {
    }

    record ShouldQueryData(float[] query, int limit, boolean with_payload, boolean with_vector,
                           List<KeyMatchData> should) {
    }

    record QueryResponse(List<Chunk> result) {
    }
}

/*
{
  "query": [0.12, 0.55, 0.87],
  "filter": {
    "should": [
      {
        "key": "type",
        "match": {
          "value": "article"
        }
      },
      {
        "key": "type",
        "match": {
          "value": "blog"
        }
      }
    ]
  }
}










{
  "query": [0.12, 0.55, 0.87],
  "limit": 10,
  "filter": {
    "must": [
      {
        "key": "type",
        "match": {
          "value": "article"
        }
      },
      {
        "key": "tenant",
        "match": {
          "value": "company-a"
        }
      }
    ]
  }
}


PUT /collections/java-books
{
  "vectors": {
    "size": 384,
    "distance": "Cosine"
  }
}


PUT /collections/java-books/points
{
  "points": [
    {
      "id": 1,
      "vector": [0.1, 0.2, 0.3],
      "payload": {
        "text": "Java supports inheritance.",
        "page": 5,
        "file": "book.pdf"
      }
    }
  ]
}


POST /collections/java-books/points/search
{
  "vector":[0.12,-0.44,0.71],
  "limit":5,
  "with_payload":true,
  "with_vector":true,
  "score_threshold":0.75,
    "filter": {
    "must": [
      {
        "key": "subject",
        "match": {
          "value": "Java"
        }
      },
      {
        "key": "document",
        "match": {
          "value": "Spring Boot.pdf"
        }
      }
    ]
  }
}
{
    "result":[
        {
            "id":15,
            "score":0.94,
            "payload":{
                "text":"Inheritance allows..."
            }
        },
        {
            "id":8,
            "score":0.91,
            "payload":{
                "text":"Java supports polymorphism..."
            }
        }
    ],
    "status":"ok",
    "time":0.003
}


GET /collections/java-books/points/15
{
  "result":{
      "id":15,
      "payload":{...},
      "vector":[...]
  }
}


POST /collections/java-books/points/delete
{
    "points":[15,20]
}


GET /collections
{
    "result":{
        "collections":[
            {
                "name":"java-books"
            },
            {
                "name":"python-books"
            }
        ]
    }
}


{
  "query": [0.12, 0.55, 0.87],
  "filter": {
    "must_not": [
      {
        "key": "type",
        "match": {
          "value": "draft"
        }
      }
    ]
  }
}
 */
