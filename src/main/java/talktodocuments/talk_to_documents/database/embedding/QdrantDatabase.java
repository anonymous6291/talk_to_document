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

@Service
public class QdrantDatabase {
    private static final int VECTOR_SIZE = 768;
    private static final String SIMILARITY_MATCHING_METHOD = "Cosine";
    private static final String BASE_URL = "http://127.0.0.1:6334/collections";
    private static final String ADD_COLLECTION_URL_EXTENSION = "";
    private static final String ADD_POINT_URL_EXTENSION = "/points";
    private static final String SEARCH_POINT_URL_EXTENSION = "/points/search";
    private static final String DELETE_POINT_URL_EXTENSION = "/points/delete";
    private final HttpClient httpClient;
    private final ObjectMapper jsonParser;

    QdrantDatabase() {
        httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        jsonParser = new ObjectMapper();
        jsonParser.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public boolean addCollection(String collectionName) throws Exception {
        NewCollectionRequest newCollectionRequest = new NewCollectionRequest(VECTOR_SIZE, SIMILARITY_MATCHING_METHOD);
        String requestJson = jsonParser.writeValueAsString(newCollectionRequest);
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(BASE_URL + collectionName + ADD_COLLECTION_URL_EXTENSION)).PUT(HttpRequest.BodyPublishers.ofString(requestJson)).build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return httpResponse.statusCode() == HttpStatus.OK.value();
    }
    record NewCollectionRequest(int size, String distance) {
    }
}

/*
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
