package talktodocuments.talk_to_documents.chunkers;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class NonTextFileChunker implements Chunker {
    private final TextChunker textChunker;
    private final Tika tika;

    public NonTextFileChunker(TextChunker textChunker, Chunkers chunkers) {
        this.textChunker = textChunker;
        this.tika = new Tika();
        chunkers.registerFallbackChunker(this);
    }

    @Override
    public List<String> getChunks(File input) throws Exception {
        String texts = tika.parseToString(input);
        return textChunker.getChunks(texts);
    }
}
