package talktodocuments.talk_to_documents.models.embedding;

import org.jspecify.annotations.NonNull;

import java.util.Comparator;

public record EmbeddingData(int index, float[] embedding) implements Comparable<EmbeddingData> {
    @Override
    public int compareTo(@NonNull EmbeddingData target) {
        return Comparator.comparingInt(EmbeddingData::index).compare(this, target);
    }
}
