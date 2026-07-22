package talktodocuments.talk_to_documents.chunkers;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class TextChunker {
    private static final int CHUNK_SIZE = 400;
    private static final int OVERLAP = 150;
    private static final Pattern END_OF_LINE_PATTERN = Pattern.compile("[.?!]");
    private final int chunkSize;
    private final int overlap;

    TextChunker() {
        this(CHUNK_SIZE, OVERLAP);
    }

    TextChunker(int chunkSize, int overlap) {
        if (chunkSize <= overlap) {
            throw new IllegalArgumentException("Chunk size must always be > overlap size.");
        }
        this.chunkSize = chunkSize;
        this.overlap = overlap;
    }

    public List<String> getChunks(String text) {
        List<String> chunks = new ArrayList<>();
        int n = text.length();
        int chunkLength = 0, lastEOL = -1;
        int nonOverlappingTextLength = chunkSize - overlap;
        StringBuilder chunk = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            chunk.append(c);
            chunkLength++;
            if ((c == '.' || c == '?' || c == '!') && chunkLength >= nonOverlappingTextLength && lastEOL == -1) {
                lastEOL = chunkLength;
            }
            if (chunkLength >= chunkSize) {
                chunks.add(chunk.toString());
                if (lastEOL != -1) {
                    chunk.delete(0, lastEOL);
                    chunkLength -= lastEOL;
                    lastEOL = -1;
                } else {
                    chunk.delete(0, chunkLength - 1);
                    chunkLength = 0;
                }
            }
        }
        if (!chunk.isEmpty()) {
            chunks.add(chunk.toString());
        }
        return chunks;
    }
}
