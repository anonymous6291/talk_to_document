package talktodocuments.talk_to_documents.models.embedding;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class Embedder {
    private final GemmaModel gemmaModel;

    public Embedder(GemmaModel gemmaModel) {
        this.gemmaModel = gemmaModel;
    }

    public List<float[]> getEmbeddings(List<String> inputs) throws Exception {
        List<EmbeddingData> embeddingData = gemmaModel.getEmbedding(inputs);
        embeddingData.sort(Comparator.naturalOrder());
        List<float[]> embeddings = new ArrayList<>(inputs.size());
        embeddingData.forEach(x -> embeddings.add(x.embedding()));
        return embeddings;
    }
}
