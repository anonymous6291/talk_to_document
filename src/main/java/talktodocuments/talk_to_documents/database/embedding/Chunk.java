package talktodocuments.talk_to_documents.database.embedding;

public record Chunk(String id, float[] vector, ChunkPayload payload) {
}
